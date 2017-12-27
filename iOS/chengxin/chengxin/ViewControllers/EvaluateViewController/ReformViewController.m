//
//  ReformViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "ReformViewController.h"
#import "Global.h"

@interface ReformViewController ()
{
    NSMutableArray *imageArray;
}
@end

@implementation ReformViewController
@synthesize editPictureView, heightPicView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.

    imageArray = [NSMutableArray array];
    CGRect rect = editPictureView.frame;
    editPictureView.delegate = self;
    editPictureView.maximumPictureCount = 6;
    [editPictureView setFrame:rect :imageArray];
    heightPicView.constant = editPictureView.frame.size.height;
    [self setDataValue];
    
    // setValue
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    self.errorReasonTextView.text = @"";
    self.errorBaseTextView.text = @"";
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [self.viewContent addSubview:editPictureView];
    [editPictureView bringSubviewToFront:self.addButtonView];
}
- (void)setDataValue {
    if(self.isOwnerFlag){
        self.evaluateReceiverLabel.text = self.reformAccountDic[@"targetAccountName"];
        self.evaluateSenderLabel.text = self.reformAccountDic[@"ownerName"];
        self.evaluateContentTextView.text = self.reformAccountDic[@"content"];
        self.errorReasonTextView.text = self.reformAccountDic[@"reason"];
    }else{
        self.evaluateReceiverLabel.text = self.reformAccountDic[@"estimateeName"];
        self.evaluateSenderLabel.text = self.reformAccountDic[@"estimaterName"];
        self.evaluateContentTextView.text = self.reformAccountDic[@"estimateContent"];
        self.errorReasonTextView.text = self.reformAccountDic[@"reason"];
    }
    int nKind = [self.reformAccountDic[@"kind"] intValue];
    if(nKind == 1) {
        self.evaluateTypeButton1.selected = YES;
        self.evaluateTypeButton2.selected = NO;
    }else{
        self.evaluateTypeButton1.selected = NO;
        self.evaluateTypeButton2.selected = YES;
    }
}

- (IBAction)onBackAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onEvaluateTypeAction:(id)sender {
    UIButton *typeButton = (UIButton *)sender;
    if(typeButton == self.evaluateTypeButton1) {
        if(!self.evaluateTypeButton1.selected) {
            self.evaluateTypeButton1.selected = YES;
            self.evaluateTypeButton2.selected = NO;
        }
    }else{
        if(!self.evaluateTypeButton2.selected) {
            self.evaluateTypeButton1.selected = NO;
            self.evaluateTypeButton2.selected = YES;
        }
    }
}

- (IBAction)onAddEvaluateAction:(id)sender {
    if(self.errorReasonTextView.text.length == 0) {
        [appDelegate.window makeToast:@"请输入纠错原因"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        return;
    }
    if(self.errorBaseTextView.text.length == 0) {
        [appDelegate.window makeToast:@"请输入纠错依据"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
        return;
    }
    
    [GeneralUtil showProgress];
    
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    
    [dicParams setObject:@"makeCorrect" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [dicParams setObject:self.reformAccountDic[@"id"] forKey:@"estimateId"];
    if(self.evaluateTypeButton1.selected) {
        [dicParams setObject:@"1" forKey:@"kind"];
    }else{
        [dicParams setObject:@"2" forKey:@"kind"];
    }
    [dicParams setObject:self.errorReasonTextView.text forKey:@"reason"];
    [dicParams setObject:self.errorBaseTextView.text forKey:@"whyis"];

    NSMutableDictionary *imageDictionary = [NSMutableDictionary dictionary];
    NSMutableArray *imageDataArray = [NSMutableArray array];
    for (int i = 0; i < imageArray.count; i++) {
        NSData* image = UIImagePNGRepresentation(imageArray[i]);
        [imageDataArray addObject:image];
    }
    
    [imageDictionary setObject:imageDataArray forKey:@"images"];
    
    [[WebAPI sharedInstance] sendPostRequestWithUpload:ACTION_MAKECORRECT Parameters:dicParams UploadImages: imageDictionary :^(NSObject *resObj) {
        NSDictionary *dicRes = (NSDictionary *)resObj;
        [GeneralUtil hideProgress];
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                [self.navigationController popViewControllerAnimated:YES];
            }
            else {
                [appDelegate.window makeToast:dicRes[@"msg"]
                            duration:3.0
                            position:CSToastPositionCenter
                               style:nil];
            }
        }else{
            [appDelegate.window makeToast:@"服务器繁忙，请稍后重试"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
        }
    }];
}

- (void)textViewDidChange:(UITextView *)textView {
    NSInteger textLength;
    if(textView == self.errorReasonTextView) {
        textLength = [self.errorReasonTextView.text length];
        if(textLength <= 100) {
            self.errorReasonLimitLabel.text = [NSString stringWithFormat:@"%d/100", (int)textLength];
        }
    }else if (textView == self.errorBaseTextView) {
        textLength = [self.errorBaseTextView.text length];
        if(textLength <= 100) {
            self.errorBaseLimitLabel.text = [NSString stringWithFormat:@"%d/100", (int)textLength];
        }
    }
}
- (BOOL)textView:(UITextView *)iTextView shouldChangeTextInRange:(NSRange)iRange replacementText:(NSString *)iText {
    if (iText.length < 100) {
        return YES;
    }
    iTextView.text = [iText substringToIndex:100];
    return NO;
}

-(void) changedPictureView:(NSMutableArray *)aryPicture :(int)height {
    heightPicView.constant = height;
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
