package lt.boldadmin.nexus.backend.factory

import lt.boldadmin.nexus.api.service.collaborator.CollaboratorUpdateSubscriber
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext

@Configuration
class CollaboratorUpdateSubscriberFactory(private var context: GenericApplicationContext) {

    @Bean
    fun create() = {
        mapOf(
            "workWeek" to context.getBean<CollaboratorUpdateSubscriber>(
                "collaboratorWorkEndOnOvertimeService", CollaboratorUpdateSubscriber::class.java
            )
        )
    }

}
