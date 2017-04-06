import java.nio.ByteBuffer
import scala.collection.mutable.ArrayBuffer

// for sift1b base
val bytesPerRecord = 132
val inputPath = "hdfs://master:9000/jinfeng/image/sift1b/indexed_bigann_base.bvecs"
val outputPath = "hdfs://master:9000/jinfeng/image/sift1b/centered_indexed_bigann_base.bvecs"

val dataset = sc.binaryRecords(inputPath, bytesPerRecord).map(x => x.slice(0, bytesPerRecord))

val inputRDD = dataset.map( p => {
  val id = ByteBuffer.wrap(p.slice(0, 4).reverse).getInt();

  var features = ArrayBuffer[Float]()
  for( i <- 4 until p.length){
    val floatValue = p(i).toFloat
    features += floatValue
  }
  (id, features.toArray)
}).cache

// calculate the mean
val numberOfVectors = inputRDD.count.toDouble
val sumVec = inputRDD.map(_._2.map(x => x.toLong)).reduce( (f1, f2) => {
  f1.zip(f2).map( p => (p._1 + p._2) )
})
val meanVec = sumVec.map( x => x / (numberOfVectors).toDouble)

import java.io._
val pw = new PrintWriter("mean.txt")
for(i <- 0 until sumVec.length) pw.write(meanVec(i).toString + " ")
pw.close

// the following output the zero-centered vectors:
// may not be applicable due to memory insufficiency
// val mean = sc.broadcast(meanVec).value
// val strRDD = inputRDD.map(p => {
//   var line: String = "";
//   line += p._1.toString
//   for (i <- 0 until p._2.length) {
//     line += " ";
//     line += (p._2(i) - mean(i).toFloat).toString
//   }
//   line
// })
// strRDD.saveAsTextFile(outputPath);





