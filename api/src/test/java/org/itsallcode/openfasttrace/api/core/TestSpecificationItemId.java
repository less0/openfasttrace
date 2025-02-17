package org.itsallcode.openfasttrace.api.core;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.equalTo;
import static org.itsallcode.openfasttrace.api.core.SpecificationItemId.createId;
import static org.itsallcode.openfasttrace.api.core.SpecificationItemId.parseId;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.itsallcode.openfasttrace.api.core.SpecificationItemId.Builder;
import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * [utest->dsn~md.specification-item-id-format~2]
 */
// [utest->dsn~specification-item-id~1]
class TestSpecificationItemId
{
    private static final int REVISION = 1;
    private static final String NAME = "foo";
    private static final String ARTIFACT_TYPE_FEATURE = "feat";

    @Test
    void testCreateId()
    {
        final SpecificationItemId id = createId(ARTIFACT_TYPE_FEATURE, NAME, REVISION);
        assertThat(id, equalTo(new Builder().artifactType(ARTIFACT_TYPE_FEATURE).name(NAME)
                .revision(REVISION).build()));
    }

    @Test
    void testCreateIdWithoutRevision()
    {
        final SpecificationItemId id = createId(ARTIFACT_TYPE_FEATURE, NAME);
        assertThat(id, equalTo(new Builder().artifactType(ARTIFACT_TYPE_FEATURE).name(NAME)
                .revision(Integer.MIN_VALUE).build()));
    }

    @Test
    void testParseId_singleDigitRevision()
    {
        final SpecificationItemId id = parseId("feat~foo~1");
        assertThat(id.getArtifactType(), equalTo(ARTIFACT_TYPE_FEATURE));
        assertThat(id.getName(), equalTo(NAME));
        assertThat(id.getRevision(), equalTo(1));
    }

    @Test
    void testParseId_multipleFragmentName()
    {
        final SpecificationItemId id = parseId("feat~foo.bar_zoo.baz-narf~1");
        assertThat(id.getArtifactType(), equalTo(ARTIFACT_TYPE_FEATURE));
        assertThat(id.getName(), equalTo("foo.bar_zoo.baz-narf"));
        assertThat(id.getRevision(), equalTo(1));
    }

    @Test
    void testParseId_multipleDigitRevision()
    {
        final SpecificationItemId id = parseId("feat~foo~999");
        assertThat(id.getArtifactType(), equalTo(ARTIFACT_TYPE_FEATURE));
        assertThat(id.getName(), equalTo(NAME));
        assertThat(id.getRevision(), equalTo(999));
    }

    @Test
    void testParseId_IllegalNumberFormat()
    {
        assertThrows(IllegalArgumentException.class,
                () -> parseId("feat~foo~999999999999999999999999999999999999999"));
    }

    @Test
    void testParseIdFailsForWildcardRevision()
    {
        assertThrows(IllegalStateException.class,
                () -> parseId("feat~foo~" + SpecificationItemId.REVISION_WILDCARD));
    }

    @Test
    void testParseId_mustFailForIllegalIds()
    {
        final String[] negatives = { "feat.foo~1", "foo~1", "req~foo", "req1~foo~1", "req.r~foo~1",
                "req~1foo~1", "req~.foo~1", "req~foo~-1" };

        for (final String sample : negatives)
        {
            assertParsingExceptionOnIllegalSpecificationItemId(sample);
        }
    }

    private void assertParsingExceptionOnIllegalSpecificationItemId(final String sample)
    {
        try
        {
            parseId(sample);
            fail("Expected exception trying to parse \"" + sample
                    + "\" into a specification item ID");
        }
        catch (final IllegalStateException exception)
        {
            // this block intentionally left empty
        }
    }

    @Test
    void testToRevisionWildcard()
    {
        final SpecificationItemId id = parseId("feat~foo~999");
        assertThat(id.toRevisionWildcard().getRevision(),
                equalTo(SpecificationItemId.REVISION_WILDCARD));
    }

    @Test
    void testToString()
    {
        final Builder builder = new Builder();
        builder.artifactType("dsn").name("dummy").revision(3);
        assertThat(builder.build().toString(), equalTo("dsn~dummy~3"));
    }

    @Test
    void testToStringRevisionWildcard()
    {
        final Builder builder = new Builder();
        builder.artifactType("dsn").name("dummy").revisionWildcard();
        assertThat(builder.build().toString(),
                equalTo("dsn~dummy~" + SpecificationItemId.REVISION_WILDCARD));
    }

    void testCreate_WithRevisionWildcard()
    {
        final SpecificationItemId id = createId(ARTIFACT_TYPE_FEATURE, NAME);
        assertThat(id.getArtifactType(), equalTo(ARTIFACT_TYPE_FEATURE));
        assertThat(id.getName(), equalTo(NAME));
        assertThat(id.getRevision(), equalTo(SpecificationItemId.REVISION_WILDCARD));
    }

    @Test
    void testEqualsAndHashContract()
    {
        EqualsVerifier.forClass(SpecificationItemId.class).verify();
    }
}
