package lt.boldadmin.nexus.backend.factory

import lt.boldadmin.nexus.api.service.collaborator.WorkWeekUpdateSubscriber
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext

@Configuration
class CollaboratorUpdateSubscriberFactory(private var context: GenericApplicationContext) {

    @Bean
    fun create() = {
        context.getBean<WorkWeekUpdateSubscriber>(
            "collaboratorWorkEndOnOvertimeSubscriber", WorkWeekUpdateSubscriber::class.java
        )
    }

}
