// get the first k items of a dataset
val bytesPerRecord = 132
val K = 1000
val inputPath = "hdfs://master:9000/jinfeng/image/sift1b/indexed_bigann_query.bvecs"
val outputPath = "hdfs://master:9000/jinfeng/image/sift1b/indexed_bigann_query.bvecs-first1000"

val dataset = sc.binaryRecords(inputPath, bytesPerRecord).map(x => x.slice(0, bytesPerRecord))

// sample first k items

val resultRDD = dataset.zipWithIndex().filter(_._2 < K).map(_._1).map(line => {
  (line.slice(0, 4), line.slice(4, line.length))
})
resultRDD.saveAsNewAPIHadoopFile[TeraOutputFormat](outputPath)
