import os, sys

sys.path.append('/Library/Frameworks/Python.framework/Versions/2.7/lib/python2.7/site-packages/')
from stemming.porter2 import stem
from nltk.corpus import stopwords

documents = sys.argv[1]
stop = set(stopwords.words('english'))
# #
# text ="houses"
# stop = set(stopwords.words('english'))
# tokens = stem(text)
# if tokens not in stop:
#     print tokens
#
#
# documents = "Human machine and interface for lab abc computer applications"
# print stem(documents)

documents = [stem(word) for word in documents.split(" ") if word not in stop]
print documents