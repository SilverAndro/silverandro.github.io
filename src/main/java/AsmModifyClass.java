import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AsmModifyClass {
    @SuppressWarnings("DataFlowIssue")
    public static void main(String[] args) throws IOException {
        File file = new File("Calculator.class");
        FileInputStream inputStream = new FileInputStream(file);

        ClassReader reader = new ClassReader(inputStream);
        inputStream.close();

        ClassNode classNode = new ClassNode();
        reader.accept(classNode, 0);

        for (MethodNode method : classNode.methods) {
            if (method.name.equals("stringIsInteger")) {
                InsnList secondPatch = new InsnList();
                secondPatch.add(new IntInsnNode(Opcodes.ALOAD, 0));
                secondPatch.add(new LdcInsnNode("-?\\d+"));
                secondPatch.add(
                        new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "matches", "(Ljava/lang/String;)Z")
                );
                secondPatch.add(new InsnNode(Opcodes.IRETURN));

                method.tryCatchBlocks.clear();
                method.instructions.clear();
                method.instructions.insert(secondPatch);

                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
                classNode.accept(writer);

                File outputFile = new File("calculator-modified.class");
                FileOutputStream outputStream = new FileOutputStream(outputFile);
                outputStream.write(writer.toByteArray());
                outputStream.close();
            }
        }
    }
}
