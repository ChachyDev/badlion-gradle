package org.slimepowered.gradle.tasks.setup.source.provider.cfr.sink

import org.benf.cfr.reader.api.OutputSinkFactory
import org.benf.cfr.reader.api.SinkReturns

class CFROutputSink : OutputSinkFactory {
    var decompiled: String? = null

    override fun getSupportedSinks(
        sinkType: OutputSinkFactory.SinkType,
        collection: Collection<OutputSinkFactory.SinkClass>
    ): List<OutputSinkFactory.SinkClass> {
        return if (sinkType == OutputSinkFactory.SinkType.JAVA && collection.contains(OutputSinkFactory.SinkClass.DECOMPILED)) {
            listOf(OutputSinkFactory.SinkClass.DECOMPILED, OutputSinkFactory.SinkClass.STRING)
        } else listOf(OutputSinkFactory.SinkClass.STRING)
    }


    override fun <T> getSink(
        sinkType: OutputSinkFactory.SinkType,
        sinkClass: OutputSinkFactory.SinkClass
    ): OutputSinkFactory.Sink<T> {
        return if (sinkType == OutputSinkFactory.SinkType.JAVA && sinkClass == OutputSinkFactory.SinkClass.DECOMPILED) {
            OutputSinkFactory.Sink {
                (it as SinkReturns.Decompiled).let { d ->
                    decompiled = d.java
                }
            }
        } else NoopSink()
    }
}