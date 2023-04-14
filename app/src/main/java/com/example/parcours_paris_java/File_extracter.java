package com.example.parcours_paris_java;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class File_extracter {


    private Context context;
    private AssetManager assetManager;

    private InputStream inputStream;

    private BufferedReader Bfr;

    public File_extracter(  AssetManager assetManager, String path) throws IOException {

        this.assetManager = assetManager;
        this.inputStream = assetManager.open(path);
        this.Bfr =  new BufferedReader( new InputStreamReader(this.inputStream));
    }



    public Map extract_name() throws IOException {

        String line = "";
        Map<String, String> map = new HashMap<String, String>();

        while (Bfr.ready()) {
           line = Bfr.readLine();

            map.put(line.substring(0,2), line.substring(5,line.length()));

        }

        return map;
    }


}
