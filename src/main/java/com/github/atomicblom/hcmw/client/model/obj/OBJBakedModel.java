package com.github.atomicblom.hcmw.client.model.obj;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelStateComposition;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.Properties;
import org.apache.commons.lang3.tuple.Pair;
import javax.vecmath.Matrix4f;
import java.util.*;

public class OBJBakedModel implements IBakedModel
{
    private final OBJModel model;
    private IModelState state;
    private final VertexFormat format;
    private ImmutableList<BakedQuad> quads;
    private ImmutableMap<String, TextureAtlasSprite> textures;
    private TextureAtlasSprite sprite = ModelLoader.White.INSTANCE;

    public OBJBakedModel(OBJModel model, IModelState state, VertexFormat format, ImmutableMap<String, TextureAtlasSprite> textures)
    {
        this.model = model;
        this.state = state;
        if (this.state instanceof OBJState) this.updateStateVisibilityMap((OBJState) this.state);
        this.format = format;
        this.textures = textures;
    }

    public void scheduleRebake()
    {
    }

    // FIXME: merge with getQuads
    @Override
    public List<BakedQuad> getQuads(IBlockState blockState, EnumFacing side, long rand)
    {
        if (side != null) return ImmutableList.of();
        if (quads == null)
        {
            quads = buildQuads(this.state);
        }
        if (blockState instanceof IExtendedBlockState)
        {
            IExtendedBlockState exState = (IExtendedBlockState) blockState;
            if (exState.getUnlistedNames().contains(Properties.AnimationProperty))
            {

                IModelState newState = exState.getValue(Properties.AnimationProperty);
                if (newState != null)
                {
                    newState = new ModelStateComposition(this.state, newState);
                    return buildQuads(newState);
                }
            }
        }
        return quads;
    }

    private ImmutableList<BakedQuad> buildQuads(IModelState modelState)
    {
        List<BakedQuad> quads = Lists.newArrayList();
        Collections.synchronizedSet(new LinkedHashSet<BakedQuad>());
        Set<Face> faces = Collections.synchronizedSet(new LinkedHashSet<Face>());
        Optional<TRSRTransformation> transform = Optional.empty();
        for (Group g : this.model.getMatLib().getGroups().values())
        {
//                g.minUVBounds = this.model.getMatLib().minUVBounds;
//                g.maxUVBounds = this.model.getMatLib().maxUVBounds;
//                FMLLog.info("Group: %s u: [%f, %f] v: [%f, %f]", g.name, g.minUVBounds[0], g.maxUVBounds[0], g.minUVBounds[1], g.maxUVBounds[1]);

            if(modelState.apply(Optional.of(Models.getHiddenModelPart(ImmutableList.of(g.getName())))).isPresent())
            {
                continue;
            }
            if (modelState instanceof OBJState)
            {
                OBJState state = (OBJState) modelState;
                if (state.parent != null)
                {
                    transform = state.parent.apply(Optional.empty());
                }
                //TODO: can this be replaced by updateStateVisibilityMap(OBJState)?
                if (state.getGroupNamesFromMap().contains(Group.ALL))
                {
                    state.visibilityMap.clear();
                    for (String s : this.model.getMatLib().getGroups().keySet())
                    {
                        state.visibilityMap.put(s, state.operation.performOperation(true));
                    }
                }
                else if (state.getGroupNamesFromMap().contains(Group.ALL_EXCEPT))
                {
                    List<String> exceptList = state.getGroupNamesFromMap().subList(1, state.getGroupNamesFromMap().size());
                    state.visibilityMap.clear();
                    for (String s : this.model.getMatLib().getGroups().keySet())
                    {
                        if (!exceptList.contains(s))
                        {
                            state.visibilityMap.put(s, state.operation.performOperation(true));
                        }
                    }
                }
                else
                {
                    for (String s : state.visibilityMap.keySet())
                    {
                        state.visibilityMap.put(s, state.operation.performOperation(state.visibilityMap.get(s)));
                    }
                }
                if (state.getGroupsWithVisibility(true).contains(g.getName()))
                {
                    faces.addAll(g.applyTransform(transform));
                }
            }
            else
            {
                transform = modelState.apply(Optional.empty());
                faces.addAll(g.applyTransform(transform));
            }
        }
        for (Face f : faces)
        {
            final Vertex[] vertices = f.getVertices();
            if (this.model.getMatLib().getMaterials().get(f.getMaterialName()).isWhite())
            {
                for (Vertex v : vertices)
                {//update material in each vertex
                    if (!v.getMaterial().equals(this.model.getMatLib().getMaterial(v.getMaterial().getName())))
                    {
                        v.setMaterial(this.model.getMatLib().getMaterial(v.getMaterial().getName()));
                    }
                }
                sprite = ModelLoader.White.INSTANCE;
            }
            else sprite = this.textures.get(f.getMaterialName());
            UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
            builder.setContractUVs(true);
            builder.setQuadOrientation(EnumFacing.getFacingFromVector(f.getNormal().x, f.getNormal().y, f.getNormal().z));
            builder.setTexture(sprite);
            Normal faceNormal = f.getNormal();
            putVertexData(builder, vertices[0], faceNormal, TextureCoordinate.getDefaultUVs()[0], sprite);
            putVertexData(builder, vertices[1], faceNormal, TextureCoordinate.getDefaultUVs()[1], sprite);
            putVertexData(builder, vertices[2], faceNormal, TextureCoordinate.getDefaultUVs()[2], sprite);
            putVertexData(builder, vertices[3], faceNormal, TextureCoordinate.getDefaultUVs()[3], sprite);
            quads.add(builder.build());
        }

        if (this.textures.containsKey("particles")) {
            sprite = this.textures.get("particles");
        }

        return ImmutableList.copyOf(quads);
    }

    private final void putVertexData(UnpackedBakedQuad.Builder builder, Vertex v, Normal faceNormal, TextureCoordinate defUV, TextureAtlasSprite sprite)
    {
        for (int e = 0; e < format.getElementCount(); e++)
        {
            switch (format.getElement(e).getUsage())
            {
                case POSITION:
                    builder.put(e, v.getPos().x, v.getPos().y, v.getPos().z, v.getPos().w);
                    break;
                case COLOR:
                    if (v.getMaterial() != null)
                        builder.put(e,
                                v.getMaterial().getColor().x,
                                v.getMaterial().getColor().y,
                                v.getMaterial().getColor().z,
                                v.getMaterial().getColor().w);
                    else
                        builder.put(e, 1, 1, 1, 1);
                    break;
                case UV:
                    if (!v.hasTextureCoordinate())
                        builder.put(e,
                                sprite.getInterpolatedU(defUV.u * 16),
                                sprite.getInterpolatedV((model.getCustomData().flipV ? 1 - defUV.v: defUV.v) * 16),
                                0, 1);
                    else
                        builder.put(e,
                                sprite.getInterpolatedU(v.getTextureCoordinate().u * 16),
                                sprite.getInterpolatedV((model.getCustomData().flipV ? 1 - v.getTextureCoordinate().v : v.getTextureCoordinate().v) * 16),
                                0, 1);
                    break;
                case NORMAL:
                    if (!v.hasNormal())
                        builder.put(e, faceNormal.x, faceNormal.y, faceNormal.z, 0);
                    else
                        builder.put(e, v.getNormal().x, v.getNormal().y, v.getNormal().z, 0);
                    break;
                default:
                    builder.put(e);
            }
        }
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return model != null ? model.getCustomData().ambientOcclusion : true;
    }

    @Override
    public boolean isGui3d()
    {
        return model != null ? model.getCustomData().gui3d : true;
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return this.sprite;
    }

    // FIXME: merge with getQuads
    /* @Override
    public OBJBakedModel handleBlockState(IBlockState state)
    {
        if (state instanceof IExtendedBlockState)
        {
            IExtendedBlockState exState = (IExtendedBlockState) state;
            if (exState.getUnlistedNames().contains(OBJProperty.instance))
            {
                OBJState s = exState.getValue(OBJProperty.instance);
                if (s != null)
                {
                    if (s.visibilityMap.containsKey(Group.ALL) || s.visibilityMap.containsKey(Group.ALL_EXCEPT))
                    {
                        this.updateStateVisibilityMap(s);
                    }
                    return getCachedModel(s);
                }
            }
        }
        return this;
    }*/

    private void updateStateVisibilityMap(OBJState state)
    {
        if (state.visibilityMap.containsKey(Group.ALL))
        {
            boolean operation = state.visibilityMap.get(Group.ALL);
            state.visibilityMap.clear();
            for (String s : this.model.getMatLib().getGroups().keySet())
            {
                state.visibilityMap.put(s,  state.operation.performOperation(operation));
            }
        }
        else if (state.visibilityMap.containsKey(Group.ALL_EXCEPT))
        {
            List<String> exceptList = state.getGroupNamesFromMap().subList(1, state.getGroupNamesFromMap().size());
            state.visibilityMap.remove(Group.ALL_EXCEPT);
            for (String s : this.model.getMatLib().getGroups().keySet())
            {
                if (!exceptList.contains(s))
                {
                    state.visibilityMap.put(s, state.operation.performOperation(state.visibilityMap.get(s)));
                }
            }
        }
        else
        {
            for (String s : state.visibilityMap.keySet())
            {
                state.visibilityMap.put(s, state.operation.performOperation(state.visibilityMap.get(s)));
            }
        }
    }

    private final LoadingCache<IModelState, OBJBakedModel> cache = CacheBuilder.newBuilder().maximumSize(20).build(new CacheLoader<IModelState, OBJBakedModel>()
    {
        @Override
        public OBJBakedModel load(IModelState state) throws Exception
        {
            return new OBJBakedModel(model, state, format, textures);
        }
    });

    public OBJBakedModel getCachedModel(IModelState state)
    {
        return cache.getUnchecked(state);
    }

    public OBJModel getModel()
    {
        return this.model;
    }

    public IModelState getState()
    {
        return this.state;
    }

    public OBJBakedModel getBakedModel()
    {
        return new OBJBakedModel(this.model, this.state, this.format, this.textures);
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType)
    {
        return PerspectiveMapWrapper.handlePerspective(this, state, cameraTransformType);
    }

    @Override
    public String toString()
    {
        return this.model.getModelLocation().toString();
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return ItemOverrideList.NONE;
    }
}
