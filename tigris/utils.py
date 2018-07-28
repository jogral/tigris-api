import random
import string


def generate_rand_str(N):
    return ''.join(
        random.SystemRandom().choice(
            string.ascii_uppercase + string.digits
        )
        for _ in range(N)
    )
