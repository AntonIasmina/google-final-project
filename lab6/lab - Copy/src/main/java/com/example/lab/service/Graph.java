package com.example.lab.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Graph {
    private int nrNodes;
    private LinkedList<Integer>[] adjacent;
    ArrayList<ArrayList<Integer> > components = new ArrayList<>();

    public Graph(int nrNodes)
    {
        this.nrNodes = nrNodes;
        adjacent = new LinkedList[nrNodes];

        for (int i = 0; i < nrNodes; i++)
            adjacent[i] = new LinkedList();
    }

    public void addEdge(int u, int w)
    {
        adjacent[u].add(w);
    }

    public void DFSUtil(int v, boolean[] visited, ArrayList<Integer> al)
    {
        visited[v] = true;
        al.add(v);

        Iterator<Integer> it = adjacent[v].iterator();

        while (it.hasNext()) {
            int n = it.next();
            if (!visited[n])
                DFSUtil(n, visited, al);
        }
    }

    public void DFS()
    {
        boolean[] visited = new boolean[nrNodes];

        for (int i = 0; i < nrNodes; i++) {
            ArrayList<Integer> al = new ArrayList<>();
            if (!visited[i]) {
                DFSUtil(i, visited, al);
                components.add(al);
            }
        }
    }

    public int ConnecetedComponents() { return components.size(); }
}