import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.cast
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.isAccessible

object CopyUtils {
    /**
     * Creates clone of the object
     */
    fun  deepClone(obj: Any): Any {
        val clazz = obj::class
        val constructor = clazz.getConstructor()
        val args = getArguments(constructor)
        val clone: Any?

        try {
            clone = clazz.cast(constructor.call(*args))
        } catch(exception: Exception) {
            throw Exception("Unclonable object")
        }

        clazz.memberProperties.forEach {
            it.isAccessible = true
            if (it is KMutableProperty1) {
                val value = getValue(obj, it)

                it.setter.call(clone, value)
            }
        }

        return clone
    }

    /**
     * Gets random constructor arguments
     */
    private fun getArguments(constructor: KFunction<Any>): Array<Any> {
        val arrgsList = mutableListOf<Any>()

        if (constructor.parameters.count() > 0) {
            constructor.parameters.forEach {
                when {
                    it.type.isSubtypeOf(String::class.starProjectedType) -> {
                        arrgsList.add("randomString")
                    }
                    it.type.isSubtypeOf(Char::class.starProjectedType) -> {
                        arrgsList.add('a')
                    }
                    it.type.isSubtypeOf(Int::class.starProjectedType)
                            || it.type.isSubtypeOf(Long::class.starProjectedType)
                            || it.type.isSubtypeOf(Short::class.starProjectedType) -> {
                        arrgsList.add(123)
                    }
                    it.type.isSubtypeOf(Boolean::class.starProjectedType) -> {
                        arrgsList.add(false)
                    }
                    it.type.isSubtypeOf(Double::class.starProjectedType) -> {
                        arrgsList.add(0.0)
                    }
                    it.type.isSubtypeOf(Set::class.starProjectedType) -> {
                        arrgsList.add(mutableSetOf<Any>())
                    }
                    it.type.isSubtypeOf(List::class.starProjectedType) -> {
                        arrgsList.add(mutableListOf<Any>())
                    }
                    it.type.isSubtypeOf(Map::class.starProjectedType) -> {
                        arrgsList.add(mutableMapOf<Any, Any>())
                    }
                    else -> {
                        arrgsList.add(deepClone(it))
                    }
                }
            }
        }

        return arrgsList.toTypedArray()
    }

    /**
     * Gets more compatible constructor
     */
    private fun <T : Any> KClass<T>.getConstructor(): KFunction<T> {
        var constructor = this.constructors.first()
        var argNumber = constructor.parameters.count()

        this.constructors.forEach {
            if (it.parameters.count() < argNumber) {
                constructor = it
                argNumber = it.parameters.count()
            }
        }

        return constructor
    }

    /**
     * Gets value of the property
     */
    private fun getValue(instance: Any, parameter: KProperty1<out Any, Any?>): Any? {
        val property = instance::class.memberProperties.firstOrNull { it.name === parameter.name }

        var value: Any? = null

        if (property !== null) {
            property.isAccessible = true
            value = property.getter.call(instance)
        }

        if (value == null) {
            return null
        }

        if (
            parameter.returnType.isSubtypeOf(String::class.starProjectedType)
            || parameter.returnType.isSubtypeOf(Char::class.starProjectedType)
            || parameter.returnType.isSubtypeOf(Int::class.starProjectedType)
            || parameter.returnType.isSubtypeOf(Long::class.starProjectedType)
            || parameter.returnType.isSubtypeOf(Short::class.starProjectedType)
            || parameter.returnType.isSubtypeOf(Boolean::class.starProjectedType)
            || parameter.returnType.isSubtypeOf(Double::class.starProjectedType)
            || parameter.returnType.isSubtypeOf(Set::class.starProjectedType)
            || parameter.returnType.isSubtypeOf(Map::class.starProjectedType)
            || parameter.returnType.isSubtypeOf(List::class.starProjectedType)
        ) {
            return value
        }

        return deepClone(value)
    }
}