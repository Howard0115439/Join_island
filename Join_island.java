//Assignment_3_Graph
//reference:leetcode323

import java.util.*;
import java.io.*;

public class Main
{
    static HashMap<String,List<String>> map_adj;
    static HashMap<String,Integer> map_city_nums;
    static int total_people=0,path=0,min_path=Integer.MAX_VALUE;
    static List<Integer> store_people_nums=new ArrayList<>();

    public static void main(String[] args) throws IOException
    {
//        String[] city_population={"New York : 10","Los Angeles : 20","Chicago : 30","Houston : 40"};
//        String[] road_network={"New York : Chicago","Chicago : Houston","Houston : Los Angeles"};
//
//        int n=city_population.length;
//        City[] city=adjacency_list(city_population,road_network);
//        System.out.println(countComponents(n,city));
//        System.out.println(population_per_island());
//        System.out.println(shortest_path("New York","Los Angeles",n,city));

        List<String> list1=new ArrayList<>();
        List<String> list2=new ArrayList<>();
        FileReader fr1 = new FileReader("C:\\Users\\user\\Desktop\\美國留學\\UC_Irvine\\修課資料\\Algorithms/city_population.txt");
        FileReader fr2 = new FileReader("C:\\Users\\user\\Desktop\\美國留學\\UC_Irvine\\修課資料\\Algorithms/road_network.txt");
        BufferedReader br1 = new BufferedReader(fr1);
        BufferedReader br2 = new BufferedReader(fr2);

        while (br1.ready()) {
            list1.add(br1.readLine());
        }
        while (br2.ready()) {
            list2.add(br2.readLine());
        }
        fr1.close();
        fr2.close();

        String[] city_population=list1.toArray(new String[list1.size()]);
        String[] road_network=list2.toArray(new String[list2.size()]);

        int n=list1.size();
        City[] city=adjacency_list(city_population,road_network);
        System.out.println(countComponents(n,city));
        System.out.println(population_per_island());
        System.out.println(shortest_path("New York","Philadelphia",n,city));
    }

    public static City[] adjacency_list(String[] city_population, String[] road_network)
    {
        map_adj=new HashMap<>();
        map_city_nums=new HashMap<>();

        for(int i=0;i< city_population.length;i++)
        {
            String[] each_str=city_population[i].split(":");
            String city_name=each_str[0].substring(0,each_str[0].length()-1);

            map_adj.put(city_name,new ArrayList<>());
        }

        for(int i=0;i< road_network.length;i++)
        {
            String[] each_str=road_network[i].split(":");
            String city_name1=each_str[0].substring(0,each_str[0].length()-1);
            String city_name2=each_str[1].substring(1);

            if(!map_adj.get(city_name1).contains(city_name2))    map_adj.get(city_name1).add(city_name2);
            if(!map_adj.get(city_name2).contains(city_name1))    map_adj.get(city_name2).add(city_name1);

        }

        int n=city_population.length;
        City[] city=new City[n];
        for(int i=0;i<city_population.length;i++)
        {
            String[] each_str=city_population[i].split(":");
            String city_name=each_str[0].substring(0,each_str[0].length()-1);
            int population=Integer.parseInt(each_str[1].substring(1));
            map_city_nums.put(city_name,i);
            city[i]=new City(city_name,population, map_adj.get(city_name));
        }
        return city;
    }

    public static int countComponents(int n, City[] city)
    {
        int components = 0;
        int[] visited = new int[n];


        for (int i = 0; i < n; i++)
        {
            if (visited[i] == 0)
            {
                components++;
//                System.out.println(components+" "+city[i].name+" "+city[i].list);
                dfs(city, visited, i);
                store_people_nums.add(total_people);
                total_people=0;
            }
        }
        return components;
    }

    private static void dfs(City[] city, int[] visited, int startNode)
    {
        visited[startNode] = 1;
        total_people=total_people+city[startNode].population;

        for (int i = 0; i < city[startNode].list.size(); i++)
        {
            if (visited[map_city_nums.get(city[startNode].list.get(i))] == 0)
            {
                dfs(city, visited, map_city_nums.get(city[startNode].list.get(i)));
            }
        }
    }

    public static String shortest_path(String city_a,String city_b,int n, City[] city)
    {
        int[] visited = new int[n];

        for (int i = 0; i < n; i++)
        {
            path=0;

            if (city[i].name.equals(city_a) && visited[i] == 0)
            {
                dfs_shortest_path(city, visited, i, path, city_b);
            }
        }
        return min_path==Integer.MAX_VALUE?"NaN":String.valueOf(min_path);
    }

    private static void dfs_shortest_path(City[] city, int[] visited, int startNode, int path, String city_b)
    {
        visited[startNode] = 1;
        if(city[startNode].name.equals(city_b))
        {
            min_path=Math.min(min_path,path);
            return;
        }

        for (int i = 0; i < city[startNode].list.size(); i++)
        {
            if (visited[map_city_nums.get(city[startNode].list.get(i))] == 0)
            {
                dfs_shortest_path(city, visited, map_city_nums.get(city[startNode].list.get(i)), path+1,city_b);
            }
        }
    }

    public static List<Integer> population_per_island()
    {
        return store_people_nums;
    }
}

class City
{
    List<String> list=new ArrayList<>();

    String name;
    int population;

    public City(String name, int population, List<String> list)
    {
        this.name=name;
        this.population=population;
        this.list=list;
    }
}

