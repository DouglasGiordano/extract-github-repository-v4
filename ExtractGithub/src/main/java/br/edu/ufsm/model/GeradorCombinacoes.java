package br.edu.ufsm.model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeradorCombinacoes {

    public String nameProject;
    public String color = "#31948c";
    public long id;

    public GeradorCombinacoes(String nameProject, String color, long id) {
        this.nameProject = nameProject;
        this.color = randonColor();
        this.id = id;
    }

    private String randonColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return String.format("#%02x%02x%02x", r, g, b); 
    }

    private List<String> doComputations(String[] inputString) {

        List<String> totalList = new ArrayList<String>();
        totalList.addAll(getCombinationsPerLength(inputString));
        return totalList;

    }

    private ArrayList<String> getCombinationsPerLength(
            String[] inputString) {

        ArrayList<String> combinations = new ArrayList<String>();
        ArrayList<String[]> destinos = new ArrayList<>();
        for (int i = 0; i < inputString.length; i++) {
            String[] destinoEstrutura = inputString[i].split(":");
            if (destinoEstrutura.length == 2) {
                destinos.add(destinoEstrutura);
            }
        }
        for (int j = 0; j < inputString.length; j++) {
            for (String[] destino : destinos) {
                String[] origemStrutura = inputString[j].split(":");
                if (origemStrutura.length == 2) {
                    String data = origemStrutura[1];
                    String usuario = origemStrutura[0];
                    String dataDestino = destino[1];
                    String usuarioDestino = destino[0];
                    if (data.length() == 10 && dataDestino.length() == 10) {
                        if (dataDestino.substring(0, 4).equalsIgnoreCase(data.substring(0, 4)) &&
                                dataDestino.substring(8, 10).equalsIgnoreCase(data.substring(8,10)) &&
                                dataDestino.substring(5, 7).equalsIgnoreCase(data.substring(5,7))) {
                            if (!usuario.equalsIgnoreCase(usuarioDestino)) {
                                if (!combinations.contains(usuario + "," + usuarioDestino + "," + nameProject + "," + color + "," + data + ", " + dataDestino)
                                        && !combinations.contains(usuarioDestino + "," + usuario + "," + nameProject + "," + color + "," + dataDestino + ", " + data)) {
                                    combinations.add(usuario + "," + usuarioDestino + "," + nameProject + "," + color + "," + data + ", " + dataDestino);
                                }

                            }
                        }
                    }
                }

            }
        }

        return combinations;
    }

    public void execute(List<String> listaUsuarios) {
        try {
            StringBuilder sb = new StringBuilder();
            File file = new File("/home/douglas/Documentos/Projetos/Mestrado/Projetos/rede_dados_" + nameProject+"_dia");
            System.out.println(file.getPath());
            PrintWriter writer = new PrintWriter(file);
            writer.println("Source,Target,Label,Color,TimeInterval, TimeInternal2");
            for (String usuarios : listaUsuarios) {
                if (usuarios != null && !usuarios.equalsIgnoreCase("")) {
                    List<String> cords = doComputations(usuarios.split("##"));
                    for (String c : cords) {
                        writer.println(c);
                    }
                }

            }
            writer.close();
            String everything = sb.toString();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
