package object core {

  import com.wohanley.robots.nlp.parsing._
  import com.wohanley.robots.nlp.tokens._
  import opennlp.tools.cmdline.parser.ParserTool
  import opennlp.tools.parser.Parse


  /** Find the first base form verb phrase in the text. */
  def action(text: String): Option[String] =
    ParserTool.parseLine(text, englishParser, 1).headOption.flatMap { parse =>
      (filter(isAcceptableVerbPhrase)
        (Seq())
        (parse)).headOption
    }.map { verbParse =>
      verbParse.toString()
    }

  def isAcceptableVerbPhrase(node: Parse): Boolean = {
    node.getType() == "VB" ||
    node.getType() == "VBP" ||
    (node.getType() == "VP" && (node.getChildren().headOption match {
      case Some(node) => node.getType == "VB" || node.getType() == "VBP"
      case None => false
    }))
  }
}
