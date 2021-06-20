package com.floweytf.utils.mappings;

import com.floweytf.utils.streams.InputStreamUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public enum MappingType {
    CSRG,
    SRG,
    TSRG,
    TSRG2;

    private static final Map<MappingType, Function<InputStreamUtils, Mappings>> TYPE_TO_LAMBADA = new HashMap<>();

    static {
        TYPE_TO_LAMBADA.put(CSRG, MappingsFactory::readCsrg);
        TYPE_TO_LAMBADA.put(SRG, MappingsFactory::readSrg);
        TYPE_TO_LAMBADA.put(TSRG, MappingsFactory::readTsrg);
    }

    static Mappings getMethodFromType(MappingType type, InputStreamUtils utils) {
        return TYPE_TO_LAMBADA.get(type).apply(utils);
    }
}
