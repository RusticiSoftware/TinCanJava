package com.rusticisoftware.tincan;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Group model class
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Group extends Agent {
    private final String objectType = "Group";
}
