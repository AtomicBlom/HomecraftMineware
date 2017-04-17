package com.github.atomicblom.hcmw.client.model;

import com.github.atomicblom.hcmw.block.BlockProperties;
import com.github.atomicblom.hcmw.client.model.obj.OBJBakedModel;
import com.github.atomicblom.hcmw.client.model.obj.OBJModel;
import com.github.atomicblom.hcmw.library.BlockLibrary;
import com.github.atomicblom.hcmw.library.Reference.Model;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelCustomData;
import net.minecraftforge.client.model.MultiModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
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
        final IRegistry<ModelResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();

        final IModel wallHookModel = processModel(loadModel(Model.lantern_wall_hook), flipData);
        final IModel roofHookModel = processModel(loadModel(Model.lantern_roof_hook), flipData);

        for (final IBlockState state : BlockLibrary.lantern.getBlockState().getValidStates()) {
            final EnumFacing connection = state.getValue(BlockProperties.FACING);

            final Builder<String, Pair<IModel, IModelState>> builder = new Builder<>();
            final ModelResourceLocation modelLocation = stateMapper.getModelResourceLocation(state);

            final IBakedModel bakedModel = modelRegistry.getObject(modelLocation);
            if (!(bakedModel instanceof OBJBakedModel)) {
                continue;
            }
            final OBJBakedModel objBakedModel = (OBJBakedModel) bakedModel;
            final OBJModel model = objBakedModel.getModel();

            if (connection == EnumFacing.UP)  {
                builder.put("roof_hook", Pair.of(roofHookModel, TRSRTransformation.identity()));
            } else if (connection != EnumFacing.DOWN) {
                IModelState transformation = transformations.get(connection);
                if (transformation == null) {
                    transformation = TRSRTransformation.identity();
                }

                builder.put("wall_hook", Pair.of(wallHookModel, transformation));
            }

            final IModel multiModel = new MultiModel(
                    modelLocation,
                    model,
                    objBakedModel.getState(),
                    builder.build()
            );

            modelRegistry.putObject(modelLocation,
                    multiModel.bake(objBakedModel.getState(), DefaultVertexFormats.ITEM, textureGetter::apply)
            );
        }
    }

    private static IModel processModel(IModel model, ImmutableMap<String, String> data)
    {
        if (model instanceof IModelCustomData)
        {
            return ((IModelCustomData) model).process(data);
        }

        return model;
    }
}
