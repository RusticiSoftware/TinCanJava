package com.rusticisoftware.tincan;

import java.io.IOException;
import java.util.HashMap;

public interface StatementsQueryInterface {
    TCAPIVersion getVersion();
    HashMap<String, String> toParameterMap(TCAPIVersion version) throws IOException;
}
