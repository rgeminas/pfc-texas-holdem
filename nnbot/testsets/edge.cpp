#include <iostream>
#include <cstdlib>

int main(int argc, char** argv)
{
    if (argc == 1) 
    {
        std::cout << "Format: vowels number";
        return 1;
    }
    int n = atoi(argv[1]);

    for (int i = 0; i < n; i++)
    {
        double c = static_cast<char>(std::rand() % 100) / 100.;
        double d = static_cast<char>(std::rand() % 100) / 100.;
        std::cout << c << "  " << d << " " << (c + d > 1.? "0" : "1") << std::endl;
    }
    return 0;
}
