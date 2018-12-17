package lt.boldadmin.nexus.backend.factory

import com.mongodb.MongoClient
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.WriteConcern
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.context.support.beans
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoDbFactory
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.reactive.function.client.WebClient
import java.util.*
import javax.validation.Validation

fun beans() = beans {

    bean("mongoTemplate") {
        MongoTemplate(SimpleMongoDbFactory(ref(), ref<Environment>()["MONGO_DATABASE"])).apply {
            setWriteConcern(WriteConcern.ACKNOWLEDGED)
        }
    }

    bean("mongoClient") {
        val environment = ref<Environment>()
        MongoClient(ServerAddress(environment["MONGO_HOST"]), ArrayList<MongoCredential>().apply {
            add(
                MongoCredential.createCredential(
                    environment["MONGO_USERNAME"], environment["MONGO_AUTH_DATABASE"],
                    environment["MONGO_PASSWORD"].toCharArray()
                )
            )
        })
    }

    bean("messageSource") {
        ReloadableResourceBundleMessageSource().apply {
            setBasename("messages")
            setDefaultEncoding("UTF-8")
        }
    }

    bean("webClient") {
        WebClient.create()
    }

    bean("validator") {
        Validation
            .byDefaultProvider()
            .configure()
            .constraintValidatorFactory(ref<ValidatorBeanFactory>())
            .buildValidatorFactory()
            .validator
    }

    bean("corsFilter") {
        CorsWebFilter { CorsConfiguration().applyPermitDefaultValues() }
    }

}