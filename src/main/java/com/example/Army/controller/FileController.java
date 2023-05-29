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
    public void doDownload(HttpServletRequest request, HttpServletResponse response, String filePath, TypeFile typeFile) throws IOException {
        if (typeFile == TypeFile.PDF) {
            createPDFFile(filePath);
        } else if (typeFile == TypeFile.EXCEL) {
            createExcelFile(filePath);
        } else {
            return;
        }

        // Отримати абсолютний шлях до додатку
        ServletContext context = request.getServletContext();
        String appPath = context.getRealPath("");

        // Скласти повний абсолютний шлях до файлу
        String fullPath = filePath;
        File downloadFile = new File(fullPath);
        FileInputStream inputStream = new FileInputStream(downloadFile);

        // Отримати MIME-тип файлу
        String mimeType = context.getMimeType(fullPath);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        System.out.println("MIME type: " + mimeType);

        // Встановити атрибути відповіді
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());

        // Встановити заголовки відповіді
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        response.setHeader(headerKey, headerValue);

        // Отримати потік виведення відповіді
        OutputStream outStream = response.getOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;

        // Записати байти зчитані з потоку вводу в потік виведення
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outStream.close();
    }


    private void createExcelFile(String filePath) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet spreadsheet = workbook.createSheet(" Student Data ");

        XSSFRow row;
        Map<String, Object[]> cadetData = new TreeMap<String, Object[]>();
        cadetData.put("1", new Object[]{"Name", "Surname", "City"});

        int i = 1;
        for (Cadet cadet : repository.findAll()) {
            cadetData.put(String.valueOf(i + 1), new Object[]{cadet.getName(), cadet.getSurname(), cadet.getCity()});
            i++;
        }

        Set<String> keyid = cadetData.keySet();
        int rowid = 0;

        for (String key : keyid) {
            row = spreadsheet.createRow(rowid++);
            Object[] objectArr = cadetData.get(key);
            int cellid = 0;

            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue((String) obj);
            }
        }

        FileOutputStream out = new FileOutputStream(filePath);
        workbook.write(out);
        out.close();
    }



    private void createPDFFile(String filePath) throws IOException {
        Document doc = new Document();

        try {
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));
            doc.open();
            String text = "Report\n" +
                    "from 07/28/23 to 08/08/23 cadets have a rest\n";
            Paragraph paragraph = new Paragraph(text);
            paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph.setFont(new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL));
            doc.add(paragraph);

            PdfPTable table = new PdfPTable(3);
            PdfPCell cell1 = new PdfPCell(new Phrase("Name"));
            PdfPCell cell2 = new PdfPCell(new Phrase("Surname"));
            PdfPCell cell3 = new PdfPCell(new Phrase("City"));

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);

            for (Cadet cadet : repository.findAll()) {
                PdfPCell cellName = new PdfPCell(new Phrase(cadet.getName()));
                PdfPCell cellSurname = new PdfPCell(new Phrase(cadet.getSurname()));
                PdfPCell cellCity = new PdfPCell(new Phrase(cadet.getCity()));

                table.addCell(cellName);
                table.addCell(cellSurname);
                table.addCell(cellCity);
            }

            doc.add(table);
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }
    }


