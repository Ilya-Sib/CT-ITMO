import scala.sys.process._

object Script extends App {
  for (i <- 7 to 100) {
    s"""cmd /C curl "http://1d3p.wp.codeforces.com/new" ^
       |  -H "Connection: keep-alive" ^
       |  -H "Cache-Control: max-age=0" ^
       |  -H "Upgrade-Insecure-Requests: 1" ^
       |  -H "Origin: http://1d3p.wp.codeforces.com" ^
       |  -H "Content-Type: application/x-www-form-urlencoded" ^
       |  -H "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.63 Safari/537.36" ^
       |  -H "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9" ^
       |  -H "Referer: http://1d3p.wp.codeforces.com/" ^
       |  -H "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7" ^
       |  -H "Cookie: __utma=71512449.692168624.1631278897.1631278897.1631278897.1; JSESSIONID=4647520389B79689007B7A28D33E890A" ^
       |  --data-raw "_af=34be50b38beccce4&proof=${i * i}&amount=$i&submit=Submit" ^
       |  --insecure""".stripMargin.!!
  }
}