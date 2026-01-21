package asia.castis.web_hook.serivce.external_sys;

import asia.castis.web_hook.common.SystemType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalSystemFactory {
    private final ApplicationContext context;
    public ExternalSystemService getServiceByType(SystemType type) {
        return context.getBean(type.name(), ExternalSystemService.class);
    }
}
