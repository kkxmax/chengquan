//
//  WriteEvalViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/26/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "WriteEvalViewController.h"
#import "SelectEvaluatorVC.h"
#import "Global.h"

@interface WriteEvalViewController () <UITextViewDelegate>
{
    NSMutableArray *aryEvalPicture;
    NSInteger mKind;
    NSInteger mMethod;
    NSInteger mPersonalEnterprise;
}
@end

@implementation WriteEvalViewController
@synthesize btnFrontEval, btnBackEval, btnDetailEval, btnQuickEval;
@synthesize editPictureView, heightPictureView;
@synthesize btnPersonal, btnEnterprise, txtViewReason, txtViewContent, lblEvalName;
@synthesize evalId, evalName;
@synthesize lblContentCnt, lblReasonCnt;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.

    aryEvalPicture = [[NSMutableArray alloc] init];
    mKind = 1;
    mMethod = 1;
    mPersonalEnterprise = 1;
    evalId = @"";
    evalName = @"";
    
    CGRect rect = editPictureView.frame;
    editPictureView.delegate = self;
    editPictureView.maximumPictureCount = 5;
    [editPictureView setFrame:rect :aryEvalPicture];

    heightPictureView.constant = editPictureView.frame.size.height;
    
    lblReasonCnt.text = @"0/100";
    lblContentCnt.text = @"0/100";
    
    self.btnPersonal.layer.cornerRadius = 17.f;
    self.btnEnterprise.layer.cornerRadius = 17.f;
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    if(self.evalAccountDictionary) {
        NSInteger aKind = [self.evalAccountDictionary[@"akind"] integerValue];
        if (aKind == PERSONAL_KIND) {
            lblEvalName.text = self.evalAccountDictionary[@"realname"];
        }else{
            lblEvalName.text = self.evalAccountDictionary[@"enterName"];
        }
        self.selectTypeView.hidden = YES;
        self.accountSelectButton.enabled = NO;
        evalId = [NSString stringWithFormat:@"%d", (int)([self.evalAccountDictionary[@"id"] intValue])];
    }else{
        lblEvalName.text = evalName;
    }
    if([CommonData sharedInstance].selectedEvaluatorDic) {
        if(mPersonalEnterprise == 1) {
            self.lblEvalName.text = [NSString stringWithFormat:@"%@(%@)", [CommonData sharedInstance].selectedEvaluatorDic[@"realname"], [CommonData sharedInstance].selectedEvaluatorDic[@"code"]];
        }else{
            self.lblEvalName.text = [NSString stringWithFormat:@"%@(%@)", [CommonData sharedInstance].selectedEvaluatorDic[@"enterName"], [CommonData sharedInstance].selectedEvaluatorDic[@"code"]];
        }
        evalId = [NSString stringWithFormat:@"%d", (int)([[CommonData sharedInstance].selectedEvaluatorDic[@"id"] intValue])];
    }
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (IBAction)onClickFrontEval:(id)sender {
    [btnFrontEval setSelected:YES];
    [btnBackEval setSelected:NO];
    
    mKind = 1;
}

- (IBAction)onClickBackEval:(id)sender {
    [btnFrontEval setSelected:NO];
    [btnBackEval setSelected:YES];
    
    mKind = 2;
}

- (IBAction)onClickDetailEval:(id)sender {
    [btnDetailEval setSelected:YES];
    [btnQuickEval setSelected:NO];
    self.heightReasonView.constant = 97.f;
    mMethod = 1;
}

- (IBAction)onClickQuickEval:(id)sender {
    [btnDetailEval setSelected:NO];
    [btnQuickEval setSelected:YES];
    self.heightReasonView.constant = 0.f;
    mMethod = 2;
}

- (IBAction)onClickNavBackButton:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onClickEvalNameButton:(id)sender {
    SelectEvaluatorVC *vc = [[SelectEvaluatorVC alloc] initWithNibName:@"SelectEvaluatorVC" bundle:nil];
    vc.selectType = mPersonalEnterprise;
    vc.rootVC = self;
    [self.navigationController pushViewController:vc animated:YES];
}

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    return textView.text.length + (text.length - range.length) <= 100;
}

- (void)textViewDidChange:(UITextView *)textView {
    if (textView == self.txtViewReason)
        self.lblReasonCnt.text = [NSString stringWithFormat:@"%d/100", (int)textView.text.length];
    else if (textView == self.txtViewContent)
        self.lblContentCnt.text = [NSString stringWithFormat:@"%d/100", (int)textView.text.length];
}


- (IBAction)onClickPersonalButton:(id)sender {
    mPersonalEnterprise = 1;
    [btnPersonal setBackgroundImage:[UIImage imageNamed:@"yellow-button"] forState:UIControlStateNormal];
    [btnPersonal setTitleColor:WHITE_COLOR forState:UIControlStateNormal];
    
    [btnEnterprise setBackgroundColor:BLACK_COLOR_245];
    [btnEnterprise setTitleColor:BLACK_COLOR_102 forState:UIControlStateNormal];
    [btnEnterprise setBackgroundImage:nil forState:UIControlStateNormal];
}

- (IBAction)onClickEnterpriseButton:(id)sender {
    mPersonalEnterprise = 2;
    [btnEnterprise setBackgroundImage:[UIImage imageNamed:@"yellow-button"] forState:UIControlStateNormal];
    [btnEnterprise setTitleColor:WHITE_COLOR forState:UIControlStateNormal];
    
    [btnPersonal setBackgroundColor:BLACK_COLOR_245];
    [btnPersonal setTitleColor:BLACK_COLOR_102 forState:UIControlStateNormal];
    [btnPersonal setBackgroundImage:nil forState:UIControlStateNormal];
}

- (IBAction)onClickPublishButton:(id)sender {
    [self addEvaluateToServer];
}

-(void) changedPictureView:(NSMutableArray *)aryPicture :(int)height {
    heightPictureView.constant = height;
    aryEvalPicture = aryPicture;
}

- (void) addEvaluateToServer {
    
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    
    [dicParams setObject:@"leaveEstimate" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    if (self.isEvaluate) {
        [dicParams setObject:@"1" forKey:@"type"];
        [dicParams setObject:evalId forKey:@"accountId"];
    }
    else {
        [dicParams setObject:@"2" forKey:@"type"];
        [dicParams setObject:evalId forKey:@"hotId"];
    }

    [dicParams setObject:[NSString stringWithFormat:@"%d", mKind] forKey:@"kind"];
    [dicParams setObject:[NSString stringWithFormat:@"%d", mMethod] forKey:@"method"];
    if(mMethod == 1)
        [dicParams setObject:txtViewReason.text forKey:@"reason"];
    [dicParams setObject:txtViewContent.text forKey:@"content"];
    
    NSMutableDictionary *imageDictionary = [NSMutableDictionary dictionary];
    NSMutableArray *imageDataArray = [NSMutableArray array];
    for (int i = 0; i < aryEvalPicture.count; i++) {
        NSData* image = UIImagePNGRepresentation(aryEvalPicture[i]);
        [imageDataArray addObject:image];
    }
    
    [imageDictionary setObject:imageDataArray forKey:@"images"];

    [[WebAPI sharedInstance] sendPostRequestWithUpload:ACTION_LEAVEESTIMATE Parameters:dicParams UploadImages: imageDictionary :^(NSObject *resObj) {
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

@end
