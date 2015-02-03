package org.wkhtmltox

/**
 * @author tobiasnendel
 */
class WkhtmltoxExecutor {

    String binaryPath
    WkhtmltoxWrapper wrapper

    WkhtmltoxExecutor(String binaryPath, WkhtmltoxWrapper wrapper) {

        if (!(new File(binaryPath)).exists()) {
            throw new WkhtmltoxException("Could not locate Wkhtmltox binary.")
        }

        if (!wrapper) {
            throw new WkhtmltoxException("Wrapper must be set.")
        }

        this.binaryPath = binaryPath
        this.wrapper = wrapper
    }

    byte[] generatePdf(URL url) {
        // TODO
    }

    byte[] generatePdf(String html) {
        def stderr
        try {
            def commandList = wrapper.toArgumentsList()
            commandList.add(0, binaryPath)
            commandList << "-q" << "-" << "-"
            def process = (commandList as String[]).execute()

            def stdout = new ByteArrayOutputStream()
            stderr = new ByteArrayOutputStream()
            OutputStreamWriter os = new OutputStreamWriter(process.outputStream, "UTF8")
            os.write(html)
            os.close()

            process.waitForProcessOutput(stdout, stderr)
            return stdout.toByteArray()
        }
        catch (e) {
            throw new WkhtmltoxException(e)
        }
        finally {
            if (stderr) {
                def bytes = stderr.toByteArray()
                if (bytes.length) {
                    log.error new String(bytes)
                }
            }
        }
    }
}
