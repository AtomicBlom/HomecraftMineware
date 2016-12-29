package com.github.atomicblom.hcmw.registration;

import com.github.atomicblom.hcmw.HomecraftMinewares;
import com.github.atomicblom.hcmw.client.model.HCMWMultiModel;
import com.github.atomicblom.hcmw.client.model.LanternMultiModel;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by codew on 29/12/2016.
 */
@Mod.EventBusSubscriber(modid = HomecraftMinewares.MODID, value = Side.CLIENT)
class AdditionalModelRegistration
{
    static HCMWMultiModel[] multiModels = new HCMWMultiModel[]{
            new LanternMultiModel()
    };

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event)
    {
        for (HCMWMultiModel multiModel : multiModels)
        {
            multiModel.loadModel(event);
        }
    }
}
