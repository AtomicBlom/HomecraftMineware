/*
 * This code is heavily borrowed from Immersive Engineering by BluSunrize
 *
 * It has been used with his permission
 *
 * Copyright 2016 BluSunrize, All rights Reserved
 */

package com.github.atomicblom.hcmw.client.rendering;

import com.github.atomicblom.hcmw.block.HCMWProperties;
import com.github.atomicblom.hcmw.client.rendering.Support.ComparableItemStack;
import com.github.atomicblom.hcmw.client.rendering.Support.ExtBlockstateAdapter;
import com.github.atomicblom.hcmw.client.rendering.Support.Matrix4;
import com.google.common.base.Optional;
import com.google.common.collect.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.*;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.Properties;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("deprecation")
public class IESmartObjModel extends OBJBakedModel
{
    private static final Map<ComparableItemStack, IBakedModel> cachedBakedItemModels = Maps.newConcurrentMap();
    private static final HashMap<ExtBlockstateAdapter, List<BakedQuad>> modelCache = Maps.newHashMap();
    private IBakedModel baseModel = null;
    private HashMap<TransformType, Matrix4> transformationMap = Maps.newHashMap();
    private ImmutableList<BakedQuad> bakedQuads = null;
    private TextureAtlasSprite tempSprite = null;
    private ItemStack tempStack = null;
    private IBlockState tempState = null;
    private VertexFormat format = null;

    public IESmartObjModel(IBakedModel baseModel, OBJModel model, IModelState state, VertexFormat format, ImmutableMap<String, TextureAtlasSprite> textures, HashMap<TransformType, Matrix4> transformationMap)
    {
        model.super(model, state, format, textures);
        this.baseModel = baseModel;
        this.transformationMap = transformationMap;
        this.format = format;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
    {
        if(transformationMap==null || transformationMap.isEmpty())
            return super.handlePerspective(cameraTransformType);
        //		Matrix4 matrix = new Matrix4(); //Assign Matrixes here manually in debug mode, then move them to the actual registration method
        Matrix4 matrix = transformationMap.containsKey(cameraTransformType)?transformationMap.get(cameraTransformType).copy():new Matrix4();
        if(tempStack !=null && tempStack.getItem() instanceof IOBJModelCallback)
            matrix = ((IOBJModelCallback) tempStack.getItem()).handlePerspective(tempStack, cameraTransformType, matrix);

        //Dynamic stuff to use when figurign out positioning for new items!
        //FP_R
//		if(cameraTransformType==TransformType.FIRST_PERSON_RIGHT_HAND)
//			matrix = new Matrix4().rotate(Math.toRadians(-90), 0,1,0).scale(.1875, .25, .25).translate(-.5, .4375, .5);
//		else if(cameraTransformType==TransformType.FIRST_PERSON_LEFT_HAND)//FP_L
//			matrix = new Matrix4().rotate(Math.toRadians(90), 0,1,0).scale(.1875, .25, .25).translate(.45, .4375, .5);
//		else if(cameraTransformType==TransformType.THIRD_PERSON_RIGHT_HAND) //TP_R
//			matrix = new Matrix4().translate(-.125, .125,-.125).scale(.125, .125, .125).rotate(Math.toRadians(-90), 0,1,0).rotate(Math.toRadians(-10), 0,0,1);
//		else if(cameraTransformType==TransformType.THIRD_PERSON_LEFT_HAND) //TP_L
//			matrix = new Matrix4().translate(.0, .0625,-.125).scale(.125, .125, .125).rotate(Math.toRadians(90), 0,1,0).rotate(Math.toRadians(0), 0,0,1);
//		else if(cameraTransformType==TransformType.FIXED) //FIXED
//			matrix = new Matrix4().translate(.1875, -.0781225, -.15625).scale(.2, .2, .2).rotate(Math.toRadians(-40), 0,1,0).rotate(Math.toRadians(-35), 0,0,1);
//		else if(cameraTransformType==TransformType.GUI) //INV
//			matrix = new Matrix4().translate(-.25, 0,-.0625).scale(.1875, .1875, .1875).rotate(Math.PI, 0, 1, 0).rotate(Math.toRadians(-40), 0, 0, 1);
//		else //GROUND
//			matrix = new Matrix4().translate(.125, 0, .0625).scale(.125, .125, .125);
        return Pair.of(this, matrix.toMatrix4f());
    }

    VertexFormat getFormat()
    {
        return format;
    }

    private static ComparableItemStack createComparableItemStack(ItemStack stack)
    {
        final ComparableItemStack comp = new ComparableItemStack(stack);
        if(stack.hasTagCompound() && !stack.getTagCompound().hasNoTags())
        {
            comp.setUseNBT(true);
        }
        return comp;
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return overrideList;
    }

    private final ItemOverrideList overrideList = new ItemOverrideList(Lists.newArrayList())
    {
        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity)
        {
            final ComparableItemStack comp = createComparableItemStack(stack);
            if(comp == null)
            {
                return originalModel;
            }
            if(cachedBakedItemModels.containsKey(comp))
            {
                return cachedBakedItemModels.get(comp);
            }
            if(!(originalModel instanceof IESmartObjModel))
            {
                return originalModel;
            }
            final IESmartObjModel model = (IESmartObjModel)originalModel;

            ImmutableMap.Builder<String, TextureAtlasSprite> builder = ImmutableMap.builder();
            builder.put(ModelLoader.White.LOCATION.toString(), ModelLoader.White.INSTANCE);
            final TextureAtlasSprite missing = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(new ResourceLocation("missingno").toString());

            final MaterialLibrary materialLibrary = model.getModel().getMatLib();
            for (final String materialName : materialLibrary.getMaterialNames())
            {
                TextureAtlasSprite sprite = null;

                if (stack.getItem() instanceof IOBJModelCallback)
                {
                    sprite = ((IOBJModelCallback) stack.getItem()).getTextureReplacement(stack, materialName);
                }
                if (sprite == null)
                {
                    final Material material = materialLibrary.getMaterial(materialName);
                    sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(material.getTexture().getTextureLocation().toString());
                }
                if (sprite == null)
                {
                    sprite = missing;
                }
                builder.put(materialName, sprite);
            }
            builder.put("missingno", missing);
            final IESmartObjModel bakedModel = new IESmartObjModel(model.baseModel, model.getModel(), model.getState(), model.getFormat(), builder.build(), transformationMap);
            bakedModel.tempStack = stack;
            cachedBakedItemModels.put(comp, bakedModel);
            return bakedModel;
        }
    };

    @Override
    public List<BakedQuad> getQuads(IBlockState blockState, EnumFacing side, long rand)
    {
        tempState = blockState;
        if(blockState instanceof IExtendedBlockState)
        {
            final IExtendedBlockState exState = (IExtendedBlockState) blockState;
            final ExtBlockstateAdapter adapter = new ExtBlockstateAdapter(exState, MinecraftForgeClient.getRenderLayer(), Sets.newHashSet());
            if(!modelCache.containsKey(adapter))
            {
                IESmartObjModel model = null;
                if(exState.getUnlistedNames().contains(Properties.AnimationProperty))
                {
                    IModelState s = exState.getValue(Properties.AnimationProperty);
                    if(s instanceof OBJState)
                    {
                        final OBJState objstate = (OBJState)s;
                        if(objstate.parent==null || objstate.parent==TRSRTransformation.identity())
                        {
                            objstate.parent = getState();
                        }

                        if(objstate.getVisibilityMap().containsKey(Group.ALL) || objstate.getVisibilityMap().containsKey(Group.ALL_EXCEPT))
                        {
                            updateStateVisibilityMapHCMW(objstate);
                        }

                        model = new IESmartObjModel(baseModel, getModel(), objstate, format, getTextures(), transformationMap);
                    }
                }

                if(model==null)
                {
                    model = new IESmartObjModel(baseModel, getModel(), getState(), format, getTextures(), transformationMap);
                }
                model.tempState = blockState;
                modelCache.put(adapter, model.buildQuads());
            }
            return Collections.synchronizedList(Lists.newArrayList(modelCache.get(adapter)));
        }

        if(bakedQuads==null)
        {
            bakedQuads = buildQuads();
        }

        return Collections.synchronizedList(Lists.newArrayList(bakedQuads));
    }


    private ImmutableList<BakedQuad> buildQuads()
    {
        List<BakedQuad> quads = Lists.newArrayList();
        IOBJModelCallback callback = null;
        Object callbackObject = null;

        if(tempStack !=null && tempStack.getItem() instanceof IOBJModelCallback)
        {
            callback = (IOBJModelCallback)tempStack.getItem();
            callbackObject = tempStack;
        } else if(tempState instanceof IExtendedBlockState && ((IExtendedBlockState) tempState).getUnlistedNames().contains(IOBJModelCallback.PROPERTY))
        {
            callback = ((IExtendedBlockState) tempState).getValue(IOBJModelCallback.PROPERTY);
            callbackObject = tempState;
        }

        final int maxPasses = 1;

        for(int pass=0; pass<maxPasses; pass++)
        {
            for(Group g : getModel().getMatLib().getGroups().values())
            {
                if(callback != null)
                {
                    if (!callback.shouldRenderGroup(callbackObject, g.getName()))
                    {
                        continue;
                    }
                }

//				System.out.println("pass: "+pass+", renders: "+g.getName());
                final Set<Face> faces = Collections.synchronizedSet(Sets.newLinkedHashSet());
                Optional<TRSRTransformation> transform = Optional.absent();

                if(getState() instanceof OBJState)
                {
                    final OBJState state = (OBJState) getState();
                    if(state.parent != null)
                    {
                        transform = state.parent.apply(Optional.absent());
                    }
                    if(callback != null)
                    {
                        transform = callback.applyTransformations(callbackObject, g.getName(), transform);
                    }
                    if(state.getGroupsWithVisibility(true).contains(g.getName()))
                    {
                        faces.addAll(g.applyTransform(transform));
                    }
                } else
                {
                    transform = getState().apply(Optional.absent());
                    if(callback != null)
                    {
                        transform = callback.applyTransformations(callbackObject, g.getName(), transform);
                    }
                    faces.addAll(g.applyTransform(transform));
                }

                int argb = 0xffffffff;
                if(callback != null)
                {
                    argb = callback.getRenderColour(callbackObject, g.getName());
                }

                final float[] colour = {(argb >> 16 & 255) / 255.0f, (argb >> 8 & 255) / 255.0f, (argb & 255) / 255.0f, (argb >> 24 & 255) / 255.0f};

                for(final Face face : faces)
                {
                    tempSprite = null;
                    if(getModel().getMatLib().getMaterial(face.getMaterialName()).isWhite() && !"null".equals(face.getMaterialName()))
                    {
                        for(final Vertex vertex : face.getVertices())
                        {
                            final Material material = getModel().getMatLib().getMaterial(vertex.getMaterial().getName());

                            if (!vertex.getMaterial().equals(material))
                            {
                                vertex.setMaterial(material);
                            }
                        }
                        tempSprite = ModelLoader.White.INSTANCE;
                    }
                    else
                    {
                        if(tempSprite==null && callback!=null)
                        {
                            tempSprite = callback.getTextureReplacement(callbackObject, face.getMaterialName());
                        }

                        if(tempSprite==null && tempState !=null && tempState instanceof IExtendedBlockState && ((IExtendedBlockState) tempState).getUnlistedNames().contains(HCMWProperties.OBJ_TEXTURE_REMAP))
                        {
                            final Map<String, String> map = ((IExtendedBlockState) tempState).getValue(HCMWProperties.OBJ_TEXTURE_REMAP);
                            final String s = map != null ? map.get(g.getName()) : null;

                            if(s != null)
                            {
                                tempSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(s);
                            }
                        }
                        if(tempSprite==null && !"null".equals(face.getMaterialName()))
                        {
                            tempSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(getModel().getMatLib().getMaterial(face.getMaterialName()).getTexture().getTextureLocation().toString());
                        }
                    }

                    if(tempSprite != null)
                    {
                        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(getFormat());
                        builder.setQuadOrientation(EnumFacing.getFacingFromVector(face.getNormal().x, face.getNormal().y, face.getNormal().z));
                        builder.setTexture(tempSprite);
                        builder.setQuadTint(pass);
                        final Normal faceNormal = face.getNormal();
                        final TextureCoordinate[] uvs = new TextureCoordinate[4];

                        for(int i=0; i<4; i++)
                        {
                            Vertex vertex = face.getVertices()[i];
                            //V-Flip is processed here already, rather than in the later method, since it's needed for easy UV comparissons on the Shader Layers
                            uvs[i] = vertex.hasTextureCoordinate() ? new TextureCoordinate(vertex.getTextureCoordinate().u,1-vertex.getTextureCoordinate().v,vertex.getTextureCoordinate().w) : TextureCoordinate.getDefaultUVs()[i];
                        }

                        final boolean renderFace = true;
                        if(renderFace)
                        {
                            for(int i=0; i<4; i++)
                            {
                                putVertexData(builder, face.getVertices()[i], faceNormal, uvs[i], tempSprite, colour);
                            }

                            quads.add(builder.build());
                        }
                    }
                }
            }
        }
        if(callback != null)
        {
            quads = callback.modifyQuads(callbackObject, quads);
        }
        return ImmutableList.copyOf(quads);
    }


    private void putVertexData(UnpackedBakedQuad.Builder builder, Vertex v, Normal faceNormal, TextureCoordinate texCoord, TextureAtlasSprite sprite, float[] colour)
    {
        for(int e = 0; e < format.getElementCount(); e++)
        {
            switch (format.getElement(e).getUsage())
            {
                case POSITION:
                    builder.put(e, v.getPos().x, v.getPos().y, v.getPos().z, v.getPos().w);
                    break;
                case COLOR:
                    final float diffuse;
                    if(v.hasNormal())
                    {
                        diffuse = LightUtil.diffuseLight(v.getNormal().x, v.getNormal().y, v.getNormal().z);
                    } else
                    {
                        diffuse = LightUtil.diffuseLight(faceNormal.x, faceNormal.y, faceNormal.z);
                    }

                    if(v.getMaterial() != null)
                    {
                        builder.put(e,
                                diffuse * v.getMaterial().getColor().x * colour[0],
                                diffuse * v.getMaterial().getColor().y * colour[1],
                                diffuse * v.getMaterial().getColor().z * colour[2],
                                v.getMaterial().getColor().w * colour[3]);
                    } else
                    {
                        builder.put(e, diffuse * colour[0], diffuse * colour[1], diffuse * colour[2], 1 * colour[3]);
                    }
                    break;
                case UV:
                    if(sprite==null)//Double Safety. I have no idea how it even happens, but it somehow did .-.
                        sprite = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
                    builder.put(e,
                            sprite.getInterpolatedU(texCoord.u * 16),
                            sprite.getInterpolatedV(texCoord.v * 16),
                            0, 1);
                    break;
                case NORMAL:
                    if (v.hasNormal())
                    {
                        builder.put(e, v.getNormal().x, v.getNormal().y, v.getNormal().z, 0);
                    } else
                    {
                        builder.put(e, faceNormal.x, faceNormal.y, faceNormal.z, 0);
                    }
                    break;
                default:
                    builder.put(e);
            }
        }
    }

    static int getExtendedStateHash(IExtendedBlockState state)
    {
        return state.hashCode()*31 + state.getUnlistedProperties().hashCode();
    }

    //	private final LoadingCache<Integer, IESmartObjModel> ieobjcache = CacheBuilder.newBuilder().maximumSize(20).build(new CacheLoader<Integer, IESmartObjModel>()
    //	{
    //		public IESmartObjModel load(IModelState state) throws Exception
    //		{
    //			return new IESmartObjModel(baseModel, getModel(), state, getFormat(), getTextures(), transformationMap);
    //		}
    //	});

    private void updateStateVisibilityMapHCMW(OBJState state)
    {
        final Set<String> materialKeys = getModel().getMatLib().getGroups().keySet();

        if (state.getVisibilityMap().containsKey(Group.ALL))
        {
            final boolean operation = state.getVisibilityMap().get(Group.ALL);
            state.getVisibilityMap().clear();
            for (final String s : materialKeys)
            {
                state.getVisibilityMap().put(s,  OBJState.Operation.SET_TRUE.performOperation(operation));
            }
        }
        else if (state.getVisibilityMap().containsKey(Group.ALL_EXCEPT))
        {
            final List<String> exceptList = state.getGroupNamesFromMap().subList(1, state.getGroupNamesFromMap().size());
            state.getVisibilityMap().remove(Group.ALL_EXCEPT);
            for (final String materialKey : materialKeys)
            {
                if (!exceptList.contains(materialKey))
                {
                    state.getVisibilityMap().put(materialKey, OBJState.Operation.SET_TRUE.performOperation(state.getVisibilityMap().get(materialKey)));
                }
            }
        }
        else
        {
            for (final String s : state.getVisibilityMap().keySet())
            {
                state.getVisibilityMap().put(s, OBJState.Operation.SET_TRUE.performOperation(state.getVisibilityMap().get(s)));
            }
        }
    }

    private static Field f_textures = null;
    static ImmutableMap<String, TextureAtlasSprite> getTexturesForOBJModel(IBakedModel model)
    {
        try{
            ensureTexturesField();
            return (ImmutableMap<String, TextureAtlasSprite>)f_textures.get(model);
        } catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    private ImmutableMap<String, TextureAtlasSprite> getTextures()
    {
        try{
            ensureTexturesField();
            return (ImmutableMap<String, TextureAtlasSprite>)f_textures.get(this);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


    private static final Object fieldAccsessorLock = new Object();
    private static void ensureTexturesField() throws NoSuchFieldException
    {
        synchronized (fieldAccsessorLock)
        {
            if (f_textures == null)
            {
                f_textures = OBJBakedModel.class.getDeclaredField("textures");
                f_textures.setAccessible(true);
            }
        }
    }
}