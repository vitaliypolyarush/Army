import com.example.Army.repository.CadetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class FileController {

    @Autowired
    private CadetRepository repository;

    private static final int BUFFER_SIZE = 4096;
    private String filePathPDF = "PDFFile.pdf";
    private String filePathExcel = "ExcelFile.xlsx";

    private enum TypeFile {
        PDF,
        EXCEL
    }

    // Додатковий код контролера
    // ...
}
@GetMapping("/download/pdf")
public void getPDFFile(HttpServletRequest request,
                       HttpServletResponse response){
    try {
        doDownload(request,response,filePathPDF,TypeFile.PDF);
    } catch (IOException e) {        throw new RuntimeException(e);
    }
}




@GetMapping("/download/excel")public void getExcelFile(HttpServletRequest request,
                                                       HttpServletResponse response){
    try {
        doDownload(request,response,filePathExcel,TypeFile.EXCEL);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

