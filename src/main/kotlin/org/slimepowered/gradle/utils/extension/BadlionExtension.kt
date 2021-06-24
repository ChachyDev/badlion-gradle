package org.slimepowered.gradle.utils.extension

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.slimepowered.gradle.utils.extension.environment.EnvironmentType

open class BadlionExtension(factory: ObjectFactory) {
    val minecraft: Property<String> = factory.property(String::class.java)

    val badlion: Property<String> = factory.property(String::class.java)

    val environmentType: Property<EnvironmentType> = factory
        .property(EnvironmentType::class.java)
        .value(EnvironmentType.PRODUCTION)
}