package server;

import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class Dump
    {
	public void Dump(Map<Integer, List <String>> Data)
	    {
		DumperOptions options = new DumperOptions();
		Yaml yaml = new Yaml(options);
		int i = yaml.dump(Data).length();
		options.setWidth(i);
		ServerHandler.setDump(yaml.dump(Data));
	    }

    }
