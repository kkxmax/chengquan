//
//  CertZoomViewController.h
//  chengxin
//
//  Created by common on 5/9/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CertZoomViewController : UIViewController

@property (nonatomic, retain) IBOutlet UIImageView* imgCertZoom;

-(IBAction)onEmpty:(UITapGestureRecognizer*)recognizer;
@end
