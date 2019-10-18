package lt.boldadmin.nexus.backend.factory

import lt.boldadmin.nexus.api.service.worklog.CollaboratorUpdateSubscriber
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext

@Configuration
class CollaboratorUpdateSubscriberFactory(private var context: GenericApplicationContext) {

    @Bean
    fun create() = {
        mapOf(
            "workTime.startOfDayInMinutes" to context.getBean<CollaboratorUpdateSubscriber>(
                "collaboratorWorkEndByWorkTimeStartSubscriber", CollaboratorUpdateSubscriber::class.java
            ),
            "workTime.endOfDayInMinutes" to context.getBean<CollaboratorUpdateSubscriber>(
                "collaboratorWorkEndByWorkTimeEndSubscriber", CollaboratorUpdateSubscriber::class.java
            )
        )
    }

}
