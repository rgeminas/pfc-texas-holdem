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
        char c = static_cast<char>(std::rand() % 26) + 'a';
        std::cout << c << "  " << static_cast<int>(c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') << std::endl;
    }
    return 0;
}
