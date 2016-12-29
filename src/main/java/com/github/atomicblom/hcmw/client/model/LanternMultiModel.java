package com.github.atomicblom.hcmw.client.model;

import com.github.atomicblom.hcmw.block.BlockProperties;
import com.github.atomicblom.hcmw.client.model.obj.OBJBakedModel;
import com.github.atomicblom.hcmw.client.model.obj.OBJModel;
import com.github.atomicblom.hcmw.library.BlockLibrary;
import com.github.atomicblom.hcmw.library.Reference;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.MultiModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

public class LanternMultiModel extends HCMWMultiModel {

    private static final ImmutableMap<String, String> flipData = ImmutableMap.of("flip-v", String.valueOf(true));

    private static final ImmutableMap<EnumFacing, IModelState> transformations = ImmutableMap.of(
            EnumFacing.NORTH, new TRSRTransformation(ModelRotation.X0_Y0),
            EnumFacing.SOUTH, new TRSRTransformation(ModelRotation.X0_Y180),
            EnumFacing.EAST, new TRSRTransformation(ModelRotation.X0_Y90),
            EnumFacing.WEST, new TRSRTransformation(ModelRotation.X0_Y270)
    );

    @Override
    public void loadModel(ModelBakeEvent event) {
        IRegistry<ModelResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();

        final IModel wallHookModel = processModel(loadModel(Reference.Model.lantern_wall_hook), flipData);
        final IModel roofHookModel = processModel(loadModel(Reference.Model.lantern_roof_hook), flipData);

        for (IBlockState state : BlockLibrary.lantern.getBlockState().getValidStates()) {
            final EnumFacing connection = state.getValue(BlockProperties.FACING);

            final ImmutableMap.Builder<String, Pair<IModel, IModelState>> builder = new ImmutableMap.Builder<>();
            ModelResourceLocation modelLocation = stateMapper.getModelResourceLocation(state);

            final OBJBakedModel bakedModel = (OBJBakedModel) modelRegistry.getObject(modelLocation);
            OBJModel model = bakedModel.getModel();

            if (connection == EnumFacing.UP)  {
                builder.put("roof_hook", Pair.of(roofHookModel, TRSRTransformation.identity()));
            } else if (connection != EnumFacing.DOWN) {
                IModelState transformation = transformations.get(connection);
                if (transformation == null) {
                    transformation = TRSRTransformation.identity();
                }

                builder.put("wall_hook", Pair.of(wallHookModel, transformation));
            }

            IModel multiModel = new MultiModel(
                    modelLocation,
                    model,
                    bakedModel.getState(),
                    builder.build()
            );

            modelRegistry.putObject(modelLocation,
                    multiModel.bake(bakedModel.getState(), DefaultVertexFormats.ITEM, textureGetter)
            );
        }
    }

    private static IModel processModel(IModel model, ImmutableMap<String, String> data)
    {
        if (model instanceof OBJModel)
        {
            return ((OBJModel) model).process(data);
        }

        return model;
    }
}
