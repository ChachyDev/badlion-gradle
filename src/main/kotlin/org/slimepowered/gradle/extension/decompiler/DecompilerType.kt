package org.slimepowered.gradle.extension.decompiler

import org.slimepowered.gradle.tasks.setup.source.provider.DecompilerProvider
import org.slimepowered.gradle.tasks.setup.source.provider.cfr.CFRDecompilerProvider
import org.slimepowered.gradle.tasks.setup.source.provider.procyon.ProcyonDecompilerProvider

enum class DecompilerType(val decompiler: DecompilerProvider) {
    Cfr(CFRDecompilerProvider()),
    Procyon(ProcyonDecompilerProvider());
}