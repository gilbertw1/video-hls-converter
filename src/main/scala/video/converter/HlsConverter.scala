package video.converter

import java.io.{File,BufferedWriter,FileWriter}
import scala.sys.process._
import scala.collection.JavaConversions._

import HlsConverterUtil._

object HlsConverter extends App {
  
  val inputDir = new File(args(0))
  val outDir = new File(args(1))
  val bitrates = args(2).split(",").map(_.toInt)

  outDir.mkdirs()

  inputDir.listFiles.foreach { file =>
    if(!file.isDirectory) {
      convertFile(file, inputDir, outDir, bitrates)
    }
  }
}

object HlsConverterUtil {

  def convertFile(file: File, dir: File, outDir: File, bitrates: Seq[Int]) {
    val outname = outnameForFile(file)
    val allFlags = bitrates.map(br => bitrateFlags(br, outDir, outname)).mkString
    val cmd = s"ffmpeg -i ${file.getName} ${allFlags}"
    val process = Process(cmd, dir).run
    val result = process.exitValue
    generateMasterPlaylist(outname, outDir, bitrates)

    println(s"FFMPEG Process Completed For ${file.getName}: ${result}")
  }

  def outnameForFile(file: File): String = {
    file.getName.takeWhile(_ != '.')
  }

  def generateMasterPlaylist(outname: String, dir: File, bitrates: Seq[Int]) {
    val body = bitrates.map(br => playlistEntryForBitrate(outname, br)).fold("#EXTM3U")(_+"\n"+_)
    val file = new File(dir, s"${outname}.m3u8")
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(body)
    bw.close()
  }

  def playlistEntryForBitrate(outputname: String, bitrate: Int): String = {
    s"#EXT-X-STREAM-INF:PROGRAM-ID=1,BANDWIDTH=${bitrate*1000}\n${outputname}-${bitrate}k.m3u8"
  }

  def bitrateFlags(bitrate: Int, outDir: File, outname: String): String = {
    "-vcodec libx264 " +
    "-acodec libfdk_aac " +
    "-map 0 " +
    s"-b:v ${bitrate}k " +
    "-flags -global_header " +
    "-f segment " + 
    "-segment_time 10 " +
    s"-segment_list ${outDir.getAbsolutePath}/${outname}-${bitrate}k.m3u8 " +
    "-segment_format mpegts " +
    s"${outDir.getAbsolutePath}/${outname}-${bitrate}k-%05d.ts "
  }
}