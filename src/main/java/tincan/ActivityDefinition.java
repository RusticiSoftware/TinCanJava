package tincan;

import lombok.Data;

/**
 * Activity Definition model class
 */
@Data
public class ActivityDefinition {
    private LanguageMap name;
    private LanguageMap description;
    private String type;
    private Extensions extensions;
    private InteractionType interactionType;
    private String[] correctResponsesPattern;
    private InteractionComponent[] choices;
    private InteractionComponent[] scale;
    private InteractionComponent[] source;
    private InteractionComponent[] target;
    private InteractionComponent[] steps;
}
