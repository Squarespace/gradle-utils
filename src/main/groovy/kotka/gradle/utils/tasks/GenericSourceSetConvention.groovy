package kotka.gradle.utils.tasks

import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.file.UnionFileTree
import org.gradle.api.internal.file.DefaultSourceDirectorySet
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.tasks.util.PatternFilterable
import org.gradle.api.tasks.util.PatternSet

class GenericSourceSetConvention {
    protected final SourceDirectorySet source
    protected final UnionFileTree      allSource
    protected final PatternFilterable  sourcePatterns = new PatternSet()

    GenericSourceSetConvention(String language, String pattern, String displayName,
            FileResolver fileResolver) {
        def displayString = "${displayName} ${language} source"

        source = new DefaultSourceDirectorySet(displayString, fileResolver)
        source.filter.include(pattern)
        sourcePatterns.include(pattern)

        allSource = new UnionFileTree(displayString,
            source.matching(sourcePatterns))
    }
}
