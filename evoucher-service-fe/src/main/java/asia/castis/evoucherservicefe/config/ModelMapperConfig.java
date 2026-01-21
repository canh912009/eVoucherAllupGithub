package asia.castis.evoucherservicefe.config;

import asia.castis.evoucherservicefe.common.dto.publishreq.imcoming.RequestFromBE;
import asia.castis.evoucherservicefe.common.dto.publishreq.imcoming.PublishDetail;
import asia.castis.evoucherservicefe.common.dto.publishreq.outgoing.RequestToPushAgent;
import asia.castis.evoucherservicefe.common.model.publish.PublishDetailModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setDeepCopyEnabled(true);

        modelMapper.typeMap(RequestFromBE.class, RequestToPushAgent.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getId(), RequestToPushAgent::setPublishId);
                    mapper.map(src -> src.getCampaign().getId(), RequestToPushAgent::setCampaignId);
                    mapper.map(src -> src.getCampaign().getName(), RequestToPushAgent::setCampaignName);
                });
        modelMapper.typeMap(PublishDetail.class, PublishDetailModel.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getSmsId(), PublishDetailModel::setMessageId);
                    mapper.map(src -> src.getSmsType(), PublishDetailModel::setMessageType);
                    mapper.map(src -> src.getVoucher().getId(), PublishDetailModel::setVoucherId);
                });
        return modelMapper;
    }
}
