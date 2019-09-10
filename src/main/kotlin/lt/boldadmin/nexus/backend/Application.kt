package lt.boldadmin.nexus.backend

import lt.boldadmin.nexus.api.event.SubscriptionPoller
import lt.boldadmin.nexus.backend.factory.beans
import lt.boldadmin.nexus.backend.factory.redisBeans
import org.springframework.beans.factory.getBean
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader
import org.springframework.context.support.GenericApplicationContext
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.server.adapter.WebHttpHandlerBuilder
import reactor.ipc.netty.http.server.HttpServer


fun main(args: Array<String>) {

    val context = GenericApplicationContext()
    initializeBeans(context)
    context.refresh()

    context.getBean<SubscriptionPoller>().pollInNewThread()

    val httpHandler = getWebHttpHandler(context)

    HttpServer.create(8070).startAndAwait(ReactorHttpHandlerAdapter(httpHandler))
}

private fun initializeBeans(context: GenericApplicationContext) {
    beans().initialize(context)
    redisBeans().initialize(context)
    XmlBeanDefinitionReader(context).loadBeanDefinitions("classpath:context/context.xml")
}

private fun getWebHttpHandler(context: GenericApplicationContext) =
    WebHttpHandlerBuilder
        .applicationContext(context)
        .apply { if (context.containsBean("corsFilter")) filter(context.getBean<CorsWebFilter>()) }
        .build()