package lt.boldadmin.nexus.backend.factory

import lt.boldadmin.nexus.api.validator.UniqueProjectNameValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorFactory

@Suppress("ReplaceCallWithBinaryOperator")
@Component
class ValidatorBeanFactory: ConstraintValidatorFactory {

    @Autowired
    private var context: ApplicationContext? = null

    override fun <T: ConstraintValidator<*, *>> getInstance(type: Class<T>): T {
        return if (type.equals(UniqueProjectNameValidator::class.java))
            context!!.getBean(type)
        else
            type.getDeclaredConstructor().newInstance()
    }

    override fun releaseInstance(instance: ConstraintValidator<*, *>) {}
}