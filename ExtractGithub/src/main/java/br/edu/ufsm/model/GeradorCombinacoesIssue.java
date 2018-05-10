package br.edu.ufsm.model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeradorCombinacoesIssue {

    public String nameProject;
    public String color = "#31948c";
    public long id;

    public GeradorCombinacoesIssue(String nameProject, String color, long id) {
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
        for (int j = 0; j < inputString.length; j++) {
            for (String usuarioDestino : inputString) {
                String usuario = inputString[j];
                if (!usuario.equalsIgnoreCase(usuarioDestino)) {
                    if (!combinations.contains(usuario + "," + usuarioDestino + "," + nameProject + "," + color )
                            && !combinations.contains(usuarioDestino + "," + usuario + "," + nameProject + "," + color)) {
                        combinations.add(usuario + "," + usuarioDestino + "," + nameProject + "," + color );
                    }

                }
            }
        }

        return combinations;
    }

    public void execute(List<Object[]> listaUsuarios) {
        try {
            StringBuilder sb = new StringBuilder();
            File file = new File("/home/douglas/Documentos/Projetos/Mestrado/Projetos/rede_dados_" + nameProject + "_issue");
            System.out.println(file.getPath());
            PrintWriter writer = new PrintWriter(file);
            writer.println("Source,Target,Label,Color");
            for (Object[] objetos : listaUsuarios) {
                String usuarios = objetos[0]+"-"+objetos[1];
                if (usuarios != null && !usuarios.equalsIgnoreCase("")) {
                    String[] coordenadas = usuarios.split("-");
                    List<String> cords = doComputations(coordenadas);
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
