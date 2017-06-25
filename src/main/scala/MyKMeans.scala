package bdu
import java.io.{File, PrintWriter}
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.{SparkConf, SparkContext}

object MyKMeans {
       def main(args: Array[String]): Unit = {
    println("hello   ====== world");
    println("xxxx");
        val conf = new SparkConf().setAppName("kmeans")
    println("++++++")
    val sc = new SparkContext(conf)
    
    println("++++")
    val src = "/Users/cindychen/inveno/sophon2.0/cluster/coolpad/coolpad_20170619_50";
    val data = sc.textFile(src)
 val parsedData = data.map(s => Vectors.dense(s.split(',').map(_.trim.toDouble))).cache()
    
    //设置簇的个数为3
    val numClusters = 4
    //迭代20次
    val numIterations = 20
    //运行10次,选出最优解
    val runs = 10
    //设置初始K选取方式为k-means++
    val initMode = "k-means||"
    val clusters = new KMeans().
      setInitializationMode(initMode).
      setK(numClusters).
      setMaxIterations(numIterations).
      run(parsedData)
      
    //打印出测试数据属于哪个簇
    println("++++")
    //println(parsedData.map(v => v.toString() + " belong to cluster :" + clusters.predict(v)).collect().mkString("\n"))
    val writer = new PrintWriter(new File("/Users/cindychen/inveno/sophon2.0/cluster/coolpad/coolpad_20170619_50_spark_4.csv"))
    writer.println(parsedData.map(v => v.toString() + "," + clusters.predict(v)).collect().mkString("\n"))
    writer.close()
    
    // Evaluateclustering by computing Within Set Sum of Squared Errors
    val WSSSE = clusters.computeCost(parsedData)
    println("WithinSet Sum of Squared Errors = " + WSSSE)
    
    //打印出中心点
    println("Clustercenters:")
    for (center <- clusters.clusterCenters) {
      println(" " + center)
    }
    }
}
