/* 
 * functionality: this program is designed for sift1b. It can be simply modified to process other binary images
 *
 * input: binary image
 * output: indexed image in text
 * */
import java.nio.ByteBuffer

// for sift1b base
val bytesPerRecord = 132
val inputPath = "hdfs://master:9000/jinfeng/image/sift1b/bigann_base.bvecs"
val outputPath = "hdfs://master:9000/jinfeng/image/sift1b/indexed_bigann_base.bvecs"

// for sift1b query
// val bytesPerRecord = 132
// val inputPath = "hdfs://master:9000/jinfeng/image/sift1b/bigann_query.bvecs"
// val outputPath = "hdfs://master:9000/jinfeng/image/sift1b/indexed_bigann_query.bvecs"




// the map operation is compulsory, otherwise the serialization of bytearrays will be incorrect (maybe a bug in Spark)
val dataset = sc.binaryRecords(inputPath, bytesPerRecord).map(x => x.slice(0, bytesPerRecord))

// save as binaryFile
val result = dataset.zipWithIndex().map( p => {
  val bytes = ByteBuffer.allocate(4).putInt(p._2.toInt).array.reverse

  val features = p._1.slice(4, p._1.length)

  (bytes, features)
})
result.saveAsNewAPIHadoopFile[TeraOutputFormat](outputPath)


// save as textFile: memory could be insufficient for Spark
// val result = dataset.zipWithIndex().map( p => {
//   var str = p._2.toString
//   val features = p._1.slice(4, p._1.length).map(_.toInt.toString)
//   for ( i <- 0 until features.length) {
//     str = str.concat(" " + features(i))
//   }
//   str
// }).cache
// result.saveAsTextFile(outputPath)
