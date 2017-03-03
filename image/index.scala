import java.nio.ByteBuffer

val inputPath = "hdfs://master:9000/datasets/image/ANN_SIFT1B-raw/bigann_query.bvecs"
val outputPath = "hdfs://master:9000/indexed/ann_sift1b/bigann_query.bvecs"

val dataset = sc.newAPIHadoopFile[Array[Byte], Array[Byte], TeraInputFormat](inputPath)
val result = dataset.map(_._2).zipWithIndex().map( p => {
  val bytes = ByteBuffer.allocate(4).putInt(p._2.toInt).array.reverse
  (bytes, p._1)
})
result.saveAsNewAPIHadoopFile[TeraOutputFormat](outputPath)

