package ml.combust.mleap.springboot

import TypeConverters._
import javax.annotation.PostConstruct
import org.slf4j.LoggerFactory
import ml.combust.mleap.pb
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import scala.io.Source
import scalapb.json4s.Parser

@Component
class ModelLoader {
  @Value("${mleap.model.config:#{null}}")
  private val modelConfigPath: String = null

  private val logger = LoggerFactory.getLogger(classOf[ModelLoader])
  private val jsonParser = new Parser()
  private val timeout = 60000

  @PostConstruct
  def loadModel(): Unit = {
    if (modelConfigPath == null) {
      logger.info("Skipping loading model on startup")
    } else {
      logger.info(s"Loading model from $modelConfigPath")

      val fileSource = Source.fromFile(modelConfigPath)

      val request = try {
        fileSource.getLines.mkString
      } finally {
        fileSource.close()
      }

      StarterConfiguration.getMleapExecutor
        .loadModel(jsonParser.fromJsonString[pb.LoadModelRequest](request))(timeout)
    }
  }
}
