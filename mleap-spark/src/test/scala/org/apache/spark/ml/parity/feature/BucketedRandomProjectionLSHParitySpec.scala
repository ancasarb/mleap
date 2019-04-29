package org.apache.spark.ml.parity.feature

import org.apache.spark.ml.feature.{BucketedRandomProjectionLSH, VectorAssembler}
import org.apache.spark.ml.{Pipeline, Transformer}
import org.apache.spark.ml.parity.SparkParityBase
import org.apache.spark.sql._

/**
  * Created by hollinwilkins on 12/28/16.
  */
class BucketedRandomProjectionLSHParitySpec extends SparkParityBase {
  override val dataset: DataFrame = baseDataset.select("dti", "loan_amount")
  override val sparkTransformer: Transformer = new Pipeline().setStages(Array(new VectorAssembler().
    setInputCols(Array("dti", "loan_amount")).
    setOutputCol("features"),
    new BucketedRandomProjectionLSH().
      setInputCol("features").
      setBucketLength(2).
      setOutputCol("lsh_features"))).fit(dataset)

  override val paramsToSkipTesting = Array("seed") ++ // only affect fitting
    Array("numHashTables") // we load the randUnitVectors array directly so we don't need this param
}
