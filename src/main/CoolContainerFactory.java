package main;

import org.json.simple.parser.ContainerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

// using org.json.simple.parser.ContainerFactory to return objects that are easier to convert to/from Experiments

public class CoolContainerFactory implements ContainerFactory {
    @Override
    public Map createObjectContainer() {
        return new HashMap();
    }

    @Override
    public List creatArrayContainer() {
        return new ArrayList();
    }
}