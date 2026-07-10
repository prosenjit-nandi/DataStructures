import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const SRC_DIR = path.resolve(__dirname, '../../src/main/java/datastructure');
const OUTPUT_FILE = path.resolve(__dirname, '../src/data.json');

function parseJavadoc(content) {
  const result = {
    description: '',
    usage: '',
    summary: ''
  };

  const docMatch = content.match(/\/\*\*([\s\S]*?)\*\//);
  if (!docMatch) return result;

  const doc = docMatch[1];
  
  const descMatch = doc.match(/@description\s+(.*?)(?=\n\s*\*?\s*@|$)/s);
  if (descMatch) result.description = descMatch[1].replace(/\n\s*\*?\s*/g, ' ').trim();

  const usageMatch = doc.match(/@usage\s+(.*?)(?=\n\s*\*?\s*@|$)/s);
  if (usageMatch) result.usage = usageMatch[1].replace(/\n\s*\*?\s*/g, ' ').trim();

  const summaryMatch = doc.match(/@summary\s+(.*?)(?=\n\s*\*?\s*@|$)/s);
  if (summaryMatch) result.summary = summaryMatch[1].replace(/\n\s*\*?\s*/g, ' ').trim();

  return result;
}

function findJavaFiles(dir, fileList = []) {
  if (!fs.existsSync(dir)) return fileList;
  
  const files = fs.readdirSync(dir);
  for (const file of files) {
    const filePath = path.join(dir, file);
    const stat = fs.statSync(filePath);
    if (stat.isDirectory()) {
      findJavaFiles(filePath, fileList);
    } else if (filePath.endsWith('.java')) {
      // Exclude interface files if they are just interfaces
      if (!filePath.includes('/interfaces/')) {
        fileList.push(filePath);
      }
    }
  }
  return fileList;
}

function generateData() {
  const javaFiles = findJavaFiles(SRC_DIR);
  const dataStructures = [];

  for (const file of javaFiles) {
    const content = fs.readFileSync(file, 'utf-8');
    const { description, usage, summary } = parseJavadoc(content);
    
    // Only include if it has at least a description
    if (description) {
      const name = path.basename(file, '.java');
      dataStructures.push({
        id: name,
        name: name,
        description,
        usage,
        summary,
        code: content
      });
    }
  }

  // Sort by name
  dataStructures.sort((a, b) => a.name.localeCompare(b.name));

  fs.writeFileSync(OUTPUT_FILE, JSON.stringify(dataStructures, null, 2), 'utf-8');
  console.log(`Generated src/data.json with ${dataStructures.length} data structures.`);
}

generateData();
