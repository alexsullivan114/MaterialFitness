import os

title = input("Whats the name of the fragment?")
presenter = input("whats the name of the presenter?")
interface = input("whats the name of the fragment interface?")
package_name = input("Whats the package name?")
package = "peoples.materialfitness." + package_name

## create the package directory
package_path = 'C:\\Users\\alex\\code\\MaterialFitness\\app\\src\\main\\java\\peoples\\materialfitness\\' + package_name;
os.makedirs(package_path)

## create the fragment file
new_fragment_path = package_path + '\\' + title + '.java'
with open(new_fragment_path, 'w+') as f:
    f.write('package ' + package + ';\n\n')
    f.write('import peoples.materialfitness.Core.PresenterFactory;\n')
    f.write('import peoples.materialfitness.View.BaseFragment;\n\n')
    f.write('/**\n* Created by Alex Sullivan\n*/\n')
    f.write('public class ' + title + ' extends BaseFragment<' + presenter + '> implements ' + interface + '\n')
    f.write('{\n\n')
    f.write('    public static ' + title + ' newInstance()\n')
    f.write('    {\n        return new ' + title + '();\n    }\n\n')
    f.write('    @Override\n    protected PresenterFactory<' + presenter + '> getPresenterFactory()\n    {\n        return new ' + presenter + '.' + presenter + 'Factory();\n    }')
    f.write('\n}')

## create the interface file
interface_path = package_path + '\\' + interface + '.java'
with open(interface_path, 'w+') as f:
    f.write('package ' + package + ';\n\n')
    f.write('import peoples.materialfitness.View.BaseFragmentInterface;\n\n')
    f.write('/**\n* Created by Alex Sullivan\n*/\n')
    f.write('public interface ' + interface + ' extends BaseFragmentInterface\n')
    f.write('{\n}')

## create presenter file
presenter_path = package_path + '\\' + presenter + '.java'
with open(presenter_path, 'w+') as f:
    f.write('package ' + package + ';\n\n')
    f.write('import peoples.materialfitness.Core.BaseFragmentPresenter;\nimport peoples.materialfitness.Core.PresenterFactory;\n\n')
    f.write('/**\n* Created by Alex Sullivan\n*/\n')
    f.write('public class ' + presenter + ' extends BaseFragmentPresenter<' + interface + '>\n')
    f.write('{\n')
    f.write('    public static class ' + presenter + 'Factory implements PresenterFactory<' + presenter +'>\n')
    f.write('    {\n')
    f.write('        @Override\n')
    f.write('        public ' + presenter + ' createPresenter()\n')
    f.write('        {\n')
    f.write('            return new ' + presenter + '();\n')
    f.write('        }\n')
    f.write('    }\n')
    f.write('}')
    
