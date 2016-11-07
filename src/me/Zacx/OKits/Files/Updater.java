package me.Zacx.OKits.Files;

import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import me.Zacx.OKits.Main.Access;

public class Updater {

    final static String VERSION_URL = "https://api.spiget.org/v2/resources/" + "%%__RESOURCE__%%" + "/versions?size=" + Integer.MAX_VALUE + "&spiget__ua=SpigetDocs";
    final static String DESCRIPTION_URL = "https://api.spiget.org/v2/resources/" + "%%__RESOURCE__%%" + "/updates?size=" + Integer.MAX_VALUE + "&spiget__ua=SpigetDocs";

    public static Object[] getLastUpdate()
    {
        try
        {
            JSONArray versionsArray = (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(String.valueOf(VERSION_URL))));
            String lastVersion = ((JSONObject) versionsArray.get(versionsArray.size() - 1)).get("name").toString();

            if(Integer.parseInt(lastVersion.replaceAll("\\.","")) > Integer.parseInt(Access.c.getDescription().getVersion().replaceAll("\\.","")))
            {
                JSONArray updatesArray = (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(String.valueOf(DESCRIPTION_URL))));
                String updateName = ((JSONObject) updatesArray.get(updatesArray.size() - 1)).get("title").toString();

                Object[] update = {lastVersion, updateName};
                return update;
            }
        }
        catch (Exception e)
        {
            return new String[0];
        }

        return new String[0];
    }
}
