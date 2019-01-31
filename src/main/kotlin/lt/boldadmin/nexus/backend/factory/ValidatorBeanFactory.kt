package lt.boldadmin.nexus.backend.factory

import lt.boldadmin.nexus.api.validator.UniqueProjectNameValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorFactory

@Component
object ValidatorBeanFactory: ConstraintValidatorFactory {

    @Autowired
    private var context: ApplicationContext? = null

    override fun <T: ConstraintValidator<*, *>> getInstance(clazz: Class<T>): T {
        return if (clazz.equals(UniqueProjectNameValidator::class.java))
            context!!.getBean(clazz)
        else
            clazz.newInstance()
    }

    override fun releaseInstance(instance: ConstraintValidator<*, *>) {}
}