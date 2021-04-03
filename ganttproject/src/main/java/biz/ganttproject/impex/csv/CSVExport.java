package biz.ganttproject.impex.csv;

import biz.ganttproject.app.InternationalizationKt;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import net.sourceforge.ganttproject.io.CSVOptions;
import net.sourceforge.ganttproject.task.ResourceAssignment;

import java.io.IOException;
import java.io.OutputStream;

public abstract class CSVExport {
  protected static final Predicate<ResourceAssignment> COORDINATOR_PREDICATE = arg -> arg.isCoordinator();
  protected CSVOptions myCsvOptions;

  public CSVExport(CSVOptions csvOptions) {
    myCsvOptions = Preconditions.checkNotNull(csvOptions);
  }

  public SpreadsheetWriter createWriter(OutputStream stream, SpreadsheetFormat format) throws IOException {
    return SpreadsheetWriterFactory.Companion.getSpreadsheetWriter(stream, format, myCsvOptions);
  }

  public abstract void save(SpreadsheetWriter writer) throws IOException;

  protected String i18n(String key) {
    return InternationalizationKt.getRootLocalizer().formatText(key);
  }

}
