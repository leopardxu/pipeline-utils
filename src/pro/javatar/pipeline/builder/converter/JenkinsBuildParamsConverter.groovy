package pro.javatar.pipeline.builder.converter

import pro.javatar.pipeline.builder.model.JenkinsBuildParams
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class JenkinsBuildParamsConverter {

    void populateWithJenkinsBuildParams(def properties) {
        def params = dsl.params
        params.each{param -> replaceVariablesOrSetProperty(param, properties)}
    }

    void replaceVariablesOrSetProperty(def param, def properties) {
        dsl.echo "param.key: ${param.key}, param.value: ${param.value}"
        if (JenkinsBuildParams.hasKey(param.key)){
            if (JenkinsBuildParams.PROFILES.getKey().equalsIgnoreCase(param.key)) {
                amendAccordingToProfile(param.value, properties)
            } else {
                properties.put(param.key, param.value)
            }
        } else {
            replaceVariable(param, properties)
        }
    }

    void amendAccordingToProfile(String profileName, def properties) {
        def profile = properties.profile[profileName]
        profile.each {key, value -> properties.put(key, value)}
    }

    void replaceVariable(def param, def properties) {
        String toBeReplaced = "\${${param.key}}"
        properties.each{ key, value ->
            if(value.toString().trim().equalsIgnoreCase(toBeReplaced)) {
                properties.put(key, param.value)
            }
        }
    }

}