package lt.boldadmin.nexus.backend.factory

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.support.beans
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import java.time.Duration
import java.time.ZoneId

fun redisBeans() = beans {

    bean("redisConnectionFactory") {
        LettuceConnectionFactory(RedisStandaloneConfiguration(ref<Environment>()["REDIS_HOST"]!!))
    }

    bean("redisCacheConfiguration") {
        RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues()
    }

    bean("projectTimeCacheConfiguration") {
        ref<RedisCacheConfiguration>("redisCacheConfiguration").serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                Jackson2JsonRedisSerializer(ZoneId::class.java).apply {
                    setObjectMapper(jacksonObjectMapper().registerModule(JavaTimeModule()))
                })
        ).entryTtl(Duration.ofDays(ref<Environment>()["PROJECT_LOCATION_CACHE_TTL"]!!.toLong()))
    }

    bean("cacheManager") {
        RedisCacheManager.builder(ref<LettuceConnectionFactory>()).withInitialCacheConfigurations(
            mapOf("locationToTimeZone" to ref("projectTimeCacheConfiguration"))
        ).build()
    }

}
